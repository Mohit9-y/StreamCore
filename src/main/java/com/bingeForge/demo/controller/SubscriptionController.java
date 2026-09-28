package com.bingeForge.demo.controller;

import com.bingeForge.demo.entity.User;
import com.bingeForge.demo.enums.PaymentMethod;
import com.bingeForge.demo.service.PremiumUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final PremiumUserService premiumUserService;

    public SubscriptionController(PremiumUserService premiumUserService) {
        this.premiumUserService = premiumUserService;
    }

    @PostMapping("/purchase/{planId}")
    public ResponseEntity purchaseSubscription(
            @AuthenticationPrincipal User user,
            @PathVariable UUID planId,
            @RequestParam(defaultValue = "UPI") PaymentMethod method) {

        String resultMessage = premiumUserService.subscribeUser(user.getId(), planId, method);
        return ResponseEntity.ok(Map.of("message", resultMessage));
    }
}