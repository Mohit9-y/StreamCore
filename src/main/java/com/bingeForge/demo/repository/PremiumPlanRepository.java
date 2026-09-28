package com.bingeForge.demo.repository;

import com.bingeForge.demo.entity.PremiumPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface PremiumPlanRepository extends JpaRepository<PremiumPlan, UUID> {

    boolean existsByPlanNameIgnoreCase(String planName);

    Optional<PremiumPlan> findByPlanNameIgnoreCase(String planName);

    List<PremiumPlan> findAllByIsActiveTrue();

    List<PremiumPlan> id(UUID id);
}
