package com.bingeForge.demo.service;

import com.bingeForge.demo.entity.PremiumPlan;
import com.bingeForge.demo.entity.PremiumUser;
import com.bingeForge.demo.entity.Transaction;
import com.bingeForge.demo.enums.PaymentMethod;
import com.bingeForge.demo.enums.PlanStatus;
import com.bingeForge.demo.exception.ResourceNotFoundException;
import com.bingeForge.demo.repository.PremiumPlanRepository;
import com.bingeForge.demo.repository.PremiumUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class PremiumUserService {

    private final PremiumUserRepository premiumUserRepository;
    private final PremiumPlanRepository planRepository;
    private final TransactionService transactionService;

    public PremiumUserService(PremiumUserRepository premiumUserRepository,
                              PremiumPlanRepository planRepository,
                              TransactionService transactionService) {
        this.premiumUserRepository = premiumUserRepository;
        this.planRepository = planRepository;
        this.transactionService = transactionService;
    }

    @Transactional
    public String subscribeUser(UUID userId, UUID planId, PaymentMethod paymentMethod) {
        PremiumPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found"));

        // 1. Process Mock Payment
        Transaction tx = transactionService.recordSuccessfulSubscription(userId, planId, plan.getPrice(), paymentMethod);

        // 2. Grant Subscription Access
        PremiumUser subscription = new PremiumUser();
        subscription.setUserId(userId);
        subscription.setPlanId(planId);
        subscription.setTransactionId(tx.getId());
        subscription.setPlanStatus(PlanStatus.ACTIVE);
        subscription.setAutoRenew(true);
        subscription.setExpireAt(Instant.now().plus(plan.getDurationDays(), ChronoUnit.DAYS));

        premiumUserRepository.save(subscription);

        return "Successfully subscribed to " + plan.getPlanName() + ". TxID: " + tx.getGatewayTransactionId();
    }
}