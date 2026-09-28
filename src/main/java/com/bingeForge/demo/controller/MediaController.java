package com.bingeForge.demo.controller;

import com.bingeForge.demo.dto.movie.PlaybackResponse;
import com.bingeForge.demo.entity.Movie;
import com.bingeForge.demo.exception.ResourceNotFoundException;
import com.bingeForge.demo.repository.MovieRepository;
import com.bingeForge.demo.service.B2StorageService;
import com.bingeForge.demo.service.TranscoderService;
import com.bingeForge.demo.service.VideoStreamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    private final B2StorageService storageService;
    private final TranscoderService transcoderService;
    private final VideoStreamService videoStreamService;
    private final MovieRepository movieRepository;

    public MediaController(B2StorageService storageService,
                           TranscoderService transcoderService,
                           VideoStreamService videoStreamService,
                           MovieRepository movieRepository) {
        this.storageService = storageService;
        this.transcoderService = transcoderService;
        this.videoStreamService = videoStreamService;
        this.movieRepository = movieRepository;
    }

    @GetMapping("/upload-url")
    public ResponseEntity getUploadUrl(@RequestParam UUID movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        String rawS3Key = "raw/" + movieId + ".mp4";
        movie.setRawS3Key(rawS3Key);
        movieRepository.save(movie);

        String presignedUrl = storageService.generateUploadPresignedURL(rawS3Key, "video/mp4");
        return ResponseEntity.ok(Map.of("uploadUrl", presignedUrl, "rawS3Key", rawS3Key));
    }

    @PostMapping("/transcode/start/{movieId}")
    public ResponseEntity startTranscoding(@PathVariable UUID movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        if (movie.getRawS3Key() == null) {
            return ResponseEntity.badRequest().body("Raw S3 key not set. Upload URL must be generated first.");
        }

        // Starts async process
        transcoderService.transcodeToHls(movie.getRawS3Key(), movieId.toString());
        return ResponseEntity.ok("Transcoding queued successfully.");
    }

    @GetMapping("/play/{movieId}")
    public ResponseEntity playVideo(@PathVariable UUID movieId) {
        return ResponseEntity.ok(videoStreamService.getStreamManifestUrl(movieId));
    }
}