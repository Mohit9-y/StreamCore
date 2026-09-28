package com.bingeForge.demo.service;

import com.bingeForge.demo.dto.movie.PlaybackResponse;
import com.bingeForge.demo.entity.Movie;
import com.bingeForge.demo.enums.VideoStatus;
import com.bingeForge.demo.exception.ResourceNotFoundException;
import com.bingeForge.demo.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VideoStreamService {

    private final MovieRepository movieRepository;

    @Value("${cdn.domain:https://your-cloudflare-domain.com}")
    private String cdnDomain;

    public VideoStreamService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public PlaybackResponse getStreamManifestUrl(UUID movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        if (movie.getStatus() != VideoStatus.READY) {
            throw new IllegalStateException("Video is not ready for playback. Current status: " + movie.getStatus());
        }

        // Return CDN routed public URL. Note: B2 streams bucket folder must be public for this to work.
        String manifestUrl = cdnDomain + "/streams/" + movieId + "/master.m3u8";

        return new PlaybackResponse(movie.getTitle(), manifestUrl, movie.getDurationInSeconds());
    }
}