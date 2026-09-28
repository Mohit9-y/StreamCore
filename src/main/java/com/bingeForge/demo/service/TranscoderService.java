package com.bingeForge.demo.service;

import com.bingeForge.demo.entity.Movie;
import com.bingeForge.demo.enums.VideoStatus;
import com.bingeForge.demo.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class TranscoderService {
    private static final Logger log = LoggerFactory.getLogger(TranscoderService.class);

    private final S3Client s3Client;
    private final MovieRepository movieRepository;

    @Value("${b2.bucketName}")
    private String bucketName;

    public TranscoderService(S3Client s3Client, MovieRepository movieRepository) {
        this.s3Client = s3Client;
        this.movieRepository = movieRepository;
    }

    @Async("transcoderTaskExecutor")
    public void transcodeToHls(String rawS3Key, String videoId) {
        Path tempDir = null;
        try {
            // 1. Update DB to PROCESSING
            Movie movie = movieRepository.findById(UUID.fromString(videoId))
                    .orElseThrow(() -> new RuntimeException("Movie not found"));
            movie.setStatus(VideoStatus.PROCESSING);
            movieRepository.save(movie);

            tempDir = Files.createTempDirectory("hls_" + videoId);
            Path rawVideoPath = tempDir.resolve("input.mp4");

            // 2. Download raw file from Backblaze B2
            log.info("Downloading raw video {} for transcoding", rawS3Key);
            s3Client.getObject(
                    GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(rawS3Key)
                            .build(),
                    rawVideoPath
            );
            Path hlsOutputDir = tempDir.resolve("hls");
            Files.createDirectories(hlsOutputDir);

            // 3. Run FFmpeg HLS encoding
            log.info("Starting FFmpeg HLS encoding for videoId: {}", videoId);
            int exitCode = executeFfmpeg(rawVideoPath, hlsOutputDir);
            if (exitCode != 0) {
                throw new RuntimeException("FFmpeg failed with exit code: " + exitCode);
            }

            // 4. Generate master playlist
            String masterContent = """
                    #EXTM3U
                    #EXT-X-VERSION:3
                    #EXT-X-STREAM-INF:BANDWIDTH=3000000,RESOLUTION=1280x720
                    720p.m3u8
                    """;
            Files.writeString(hlsOutputDir.resolve("master.m3u8"), masterContent);

            // 5. Upload HLS chunks and playlist back to B2
            log.info("Uploading HLS chunks to B2 for videoId: {}", videoId);
            File[] files = hlsOutputDir.toFile().listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        String fileName = file.getName();
                        String s3TargetKey = "streams/" + videoId + "/" + fileName;
                        String contentType = fileName.endsWith(".m3u8")
                                ? "application/vnd.apple.mpegurl"
                                : "video/MP2T";

                        s3Client.putObject(
                                PutObjectRequest.builder()
                                        .bucket(bucketName)
                                        .key(s3TargetKey)
                                        .contentType(contentType)
                                        .build(),
                                RequestBody.fromFile(file)
                        );
                    }
                }
            }

            // 6. Delete the bulky raw video from B2 to free bucket storage
            log.info("Deleting raw file {} from B2 storage", rawS3Key);
            s3Client.deleteObject(
                    DeleteObjectRequest.builder()
                            .bucket(bucketName)
                            .key(rawS3Key)
                            .build()
            );

            // 7. Update DB to READY
            movie.setStatus(VideoStatus.READY);
            movieRepository.save(movie);

            log.info("Transcoding, upload, and cleanup complete for videoId: {}", videoId);

        } catch (Exception e) {
            log.error("Transcoding failed for videoId: {}", videoId, e);
            try {
                Movie failedMovie = movieRepository.findById(UUID.fromString(videoId)).orElse(null);
                if (failedMovie != null) {
                    failedMovie.setStatus(VideoStatus.FAILED);
                    movieRepository.save(failedMovie);
                }
            } catch (Exception ex) {
                log.error("Failed to update status to FAILED", ex);
            }
        } finally {
            if (tempDir != null) {
                deleteDirectoryRecursively(tempDir.toFile());
            }
        }
    }

    private int executeFfmpeg(Path rawVideoPath, Path hlsOutputDir) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "ffmpeg", "-i", rawVideoPath.toAbsolutePath().toString(),
                "-r", "24", // Force 24fps for consistent chunk boundaries
                "-vf", "scale=w=1280:h=720:force_original_aspect_ratio=decrease",
                "-c:a", "aac", "-ar", "48000", "-c:v", "h264",
                "-profile:v", "main", "-crf", "20",
                "-g", "144", "-keyint_min", "144", // 24fps * 6 seconds = 144
                "-b:v", "2800k", "-maxrate", "2996k", "-bufsize", "4200k",
                "-b:a", "128k",
                "-hls_time", "6", "-hls_playlist_type", "vod",
                "-hls_segment_filename", hlsOutputDir.resolve("720p_%03d.ts").toString(),
                hlsOutputDir.resolve("720p.m3u8").toString()
        );

        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        // Stream Gobbler to prevent OS buffer deadlock
        new Thread(() -> {
            try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.debug("[FFmpeg] {}", line);
                }
            } catch (Exception e) {
                log.error("Failed to read FFmpeg stream", e);
            }
        }).start();

        try {
            return process.waitFor();
        } finally {
            process.destroy();
        }
    }

    private void deleteDirectoryRecursively(File fileOrDir) {
        if (fileOrDir != null && fileOrDir.exists()) {
            File[] contents = fileOrDir.listFiles();
            if (contents != null) {
                for (File child : contents) {
                    deleteDirectoryRecursively(child);
                }
            }
            if (!fileOrDir.delete()) {
                log.warn("Could not delete file: {}", fileOrDir.getAbsolutePath());
            }
        }
    }
}