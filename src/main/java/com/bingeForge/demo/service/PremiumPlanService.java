package com.bingeForge.demo.service;

import com.bingeForge.demo.dto.premiumPlan.PremiumPlanReq;
import com.bingeForge.demo.dto.premiumPlan.PremiumPlanRes;
import com.bingeForge.demo.entity.PremiumPlan;
import com.bingeForge.demo.exception.ResourceNotFoundException;
import com.bingeForge.demo.mapper.PremiumPlanMapper;
import com.bingeForge.demo.repository.PremiumPlanRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PremiumPlanService {
    private final PremiumPlanRepository premiumPlanRepository;
    private final PremiumPlanMapper premiumPlanMapper;

    public PremiumPlanService(PremiumPlanRepository premiumPlanRepository,
                              PremiumPlanMapper premiumPlanMapper){
        this.premiumPlanRepository = premiumPlanRepository;
        this.premiumPlanMapper = premiumPlanMapper;
    }

    @Transactional
    public PremiumPlanRes save(PremiumPlanReq req){
        PremiumPlan premiumPlan = premiumPlanMapper.toEntity(req);
        return premiumPlanMapper.toResponse(premiumPlanRepository.save(premiumPlan));
    }

    public PremiumPlanRes get(UUID id){
        PremiumPlan plan = premiumPlanRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Premium Plan Id is not found"));

        return premiumPlanMapper.toResponse(plan);
    }

    @Transactional
    public List<PremiumPlanRes> getAllActivePlans() {
        return premiumPlanRepository.findAllByIsActiveTrue()
                .stream()
                .map(premiumPlanMapper::toResponse)
                .toList();
    }

    @Transactional
    public void delete(UUID id){
        if (!premiumPlanRepository.existsById(id)) {
            throw new ResourceNotFoundException("Premium Plan not found with id: " + id);
        }
        premiumPlanRepository.deleteById(id);
    }
}
