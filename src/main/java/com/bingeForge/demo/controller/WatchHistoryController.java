package com.bingeForge.demo.controller;

import com.bingeForge.demo.dto.history.WatchPositionRequest;
import com.bingeForge.demo.entity.User;
import com.bingeForge.demo.service.WatchHistoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/history")
public class WatchHistoryController {

    private final WatchHistoryService watchHistoryService;

    public WatchHistoryController(WatchHistoryService watchHistoryService) {
        this.watchHistoryService = watchHistoryService;
    }

    @PostMapping("/ping")
    public ResponseEntity updateProgress(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody WatchPositionRequest request) {

        watchHistoryService.updateProgress(user.getId(), request);
        return ResponseEntity.ok().build();
    }
}