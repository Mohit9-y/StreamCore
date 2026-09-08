package com.bingeForge.demo.repository;

import com.bingeForge.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Boolean existsByEmail(String email);
    Optional<User>findByEmail(String email);
    Optional<User>findByUsername(String userName);
    Boolean existsByUsername(String username);
}
