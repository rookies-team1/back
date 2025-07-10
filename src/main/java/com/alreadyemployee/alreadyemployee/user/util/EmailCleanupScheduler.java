package com.alreadyemployee.alreadyemployee.user.util;

import com.alreadyemployee.alreadyemployee.user.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class EmailCleanupScheduler {

    private final EmailVerificationRepository emailVerificationRepository;

    // 매 정각마다 실행 (예: 00:00, 01:00, ...)
    @Scheduled(cron = "0 0 * * * *")
    public void cleanExpiredVerificationCodes() {
        emailVerificationRepository.deleteAllByExpiresAtBefore(LocalDateTime.now());
        System.out.println("[스케줄러] 만료된 인증 코드 삭제 완료: " + LocalDateTime.now());
    }
}