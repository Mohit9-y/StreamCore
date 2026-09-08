package com.bingeForge.demo.repository;

import com.bingeForge.demo.entity.PremiumPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface PremiumPlanRepository extends JpaRepository<PremiumPlan, UUID> {
}
