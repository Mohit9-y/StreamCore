package com.bingeForge.demo.service;

import com.bingeForge.demo.dto.history.WatchPositionRequest;
import com.bingeForge.demo.entity.WatchHistory;
import com.bingeForge.demo.repository.WatchHistoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WatchHistoryService {

    private final WatchHistoryRepository watchHistoryRepository;

    public WatchHistoryService(WatchHistoryRepository watchHistoryRepository) {
        this.watchHistoryRepository = watchHistoryRepository;
    }

    @Transactional
    public void updateProgress(UUID userId, WatchPositionRequest request) {
        // Note: For a real app, you'd want a custom query in the repository to find by User AND ContentId
        // For simplicity in this base logic, we just create a new record if we don't handle the custom query yet.

        WatchHistory history = new WatchHistory();
        history.setUserId(userId);
        history.setContentType(request.contentType());

        if (request.contentType().name().equals("MOVIE")) {
            history.setMovieId(request.contentId());
        } else {
            history.setSeriesId(request.contentId());
        }

        history.setLastWatchedPositionSeconds(request.lastWatchedPositionSeconds());
        history.setTotalDurationSeconds(request.totalDurationSeconds());

        // Mark completed if they watched 90% of it
        boolean isCompleted = (request.totalDurationSeconds() > 0) &&
                ((double) request.lastWatchedPositionSeconds() / request.totalDurationSeconds() > 0.9);
        history.setIsCompleted(isCompleted);
        history.setLastWatchedAt(Instant.now());

        watchHistoryRepository.save(history);
    }
}