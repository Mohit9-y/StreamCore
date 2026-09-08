package com.bingeForge.demo.repository;

import com.bingeForge.demo.entity.PremiumUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface PremiumUserRepository extends JpaRepository <PremiumUser, UUID> {
}
