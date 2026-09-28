package com.bingeForge.demo.mapper;

import com.bingeForge.demo.dto.premiumPlan.PremiumPlanReq;
import com.bingeForge.demo.dto.premiumPlan.PremiumPlanRes;
import com.bingeForge.demo.entity.PremiumPlan;
import org.springframework.stereotype.Component;

@Component
public class PremiumPlanMapper {
    public PremiumPlanRes toResponse(PremiumPlan plan) {
        return PremiumPlanRes.builder()
                .id(plan.getId())
                .planName(plan.getPlanName())
                .price(plan.getPrice())
                .durationDays(plan.getDurationDays())
                .description(plan.getDescription())
                .maxScreenResolution(plan.getMaxScreenResolution())
                .maxConcurrentScreens(plan.getMaxConcurrentScreens())
                .isAdSupported(plan.getIsAdSupported())
                .isActive(plan.getIsActive())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .build();
    }

    public PremiumPlan toEntity(PremiumPlanReq req) {
        PremiumPlan plan = new PremiumPlan();
        plan.setPlanName(req.planName());
        plan.setPrice(req.price());
        plan.setDurationDays(req.durationDays());
        plan.setDescription(req.description());
        plan.setMaxScreenResolution(req.maxScreenResolution());
        plan.setMaxConcurrentScreens(req.maxConcurrentScreens());
        plan.setIsAdSupported(req.isAdSupported());
        plan.setIsActive(req.isActive());
        return plan;
    }
}
