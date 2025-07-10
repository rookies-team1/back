package com.alreadyemployee.alreadyemployee.user.repository;

import com.alreadyemployee.alreadyemployee.user.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification,Long> {
    Optional<EmailVerification> findTopByEmailOrderByIdDesc(String email);
    void deleteAllByExpiresAtBefore(LocalDateTime now);
}