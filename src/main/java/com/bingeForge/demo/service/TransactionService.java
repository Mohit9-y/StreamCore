package com.bingeForge.demo.service;

import com.bingeForge.demo.entity.Transaction;
import com.bingeForge.demo.enums.PaymentMethod;
import com.bingeForge.demo.enums.PaymentReason;
import com.bingeForge.demo.enums.PaymentStatus;
import com.bingeForge.demo.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction recordSuccessfulSubscription(UUID userId, UUID planId, BigDecimal amount, PaymentMethod method) {
        Transaction tx = new Transaction();
        tx.setUserId(userId);
        tx.setPremiumPlanId(planId);
        tx.setAmount(amount);
        tx.setPaymentMethod(method);
        tx.setPaymentReason(PaymentReason.PREMIUM);
        tx.setPaymentStatus(PaymentStatus.SUCCESS);
        tx.setGatewayTransactionId("MOCK_TX_" + UUID.randomUUID().toString().substring(0, 8));
        return transactionRepository.save(tx);
    }
}