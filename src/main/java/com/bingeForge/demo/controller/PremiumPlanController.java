package com.bingeForge.demo.controller;

import com.bingeForge.demo.dto.premiumPlan.PremiumPlanReq;
import com.bingeForge.demo.dto.premiumPlan.PremiumPlanRes;
import com.bingeForge.demo.service.PremiumPlanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/premium_plan")
public class PremiumPlanController {

    private final PremiumPlanService premiumPlanService;

    public PremiumPlanController(PremiumPlanService premiumPlanService){
        this.premiumPlanService = premiumPlanService;
    }

    @PostMapping("/save")
    public ResponseEntity<PremiumPlanRes>save(@Valid @RequestBody PremiumPlanReq req){
        return ResponseEntity.status(HttpStatus.CREATED).body(premiumPlanService.save(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PremiumPlanRes> get(@Valid @PathVariable UUID id){
        return ResponseEntity.status(HttpStatus.OK).body(premiumPlanService.get(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Valid @PathVariable UUID id){
        premiumPlanService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
