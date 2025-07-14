package com.alreadyemployee.alreadyemployee.user.service;


import com.alreadyemployee.alreadyemployee.exception.BusinessException;
import com.alreadyemployee.alreadyemployee.exception.ErrorCode;
import com.alreadyemployee.alreadyemployee.user.entity.CustomUserDetails;
import com.alreadyemployee.alreadyemployee.user.entity.RefreshToken;
import com.alreadyemployee.alreadyemployee.user.entity.User;
import com.alreadyemployee.alreadyemployee.user.repository.RefreshTokenRepository;
import com.alreadyemployee.alreadyemployee.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    // 저장 또는 갱신
    public void saveOrUpdate(Long userId, String refreshToken, long expiresInDays) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));;// 또는 userRepository.findById()

        RefreshToken token = refreshTokenRepository.findByUserId(userId)
                .orElse(new RefreshToken());

        token.setUser(user);
        token.setRefreshToken(refreshToken);
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusDays(expiresInDays));

        refreshTokenRepository.save(token);
    }

    // DB에 저장된 토큰 검증
    public void validateRefreshToken(Long userId, String requestToken) {
        RefreshToken token = refreshTokenRepository.findByUserId(userId)
                .orElseThrow();

        if (!token.getRefreshToken().equals(requestToken)) {
//            throw new BusinessException(ErrorCode.AUTH_INVALID_TOKEN);
        }
    }

    // 새 토큰 갱신
    public void updateRefreshToken(Long userId, String newRefreshToken, long expiresInDays) {
        RefreshToken token = refreshTokenRepository.findByUserId(userId)
                .orElseThrow();

        token.setRefreshToken(newRefreshToken);
        token.setExpiresAt(LocalDateTime.now().plusDays(expiresInDays));

        refreshTokenRepository.save(token);
    }

    // 로그아웃 등에서 삭제용
    public void deleteRefreshToken(Long userId) {
        refreshTokenRepository.findByUserId(userId)
                .ifPresent(refreshTokenRepository::delete);
    }

    public Long extractUserId(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getId();
    }
}
