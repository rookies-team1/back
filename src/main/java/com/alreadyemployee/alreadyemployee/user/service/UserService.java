package com.alreadyemployee.alreadyemployee.user.service;

import com.alreadyemployee.alreadyemployee.exception.BusinessException;
import com.alreadyemployee.alreadyemployee.exception.ErrorCode;
import com.alreadyemployee.alreadyemployee.jwt.JwtTokenProvider;
import com.alreadyemployee.alreadyemployee.user.controller.dto.SignInRequestDTO;
import com.alreadyemployee.alreadyemployee.user.controller.dto.SignInResponseDTO;
import com.alreadyemployee.alreadyemployee.user.controller.dto.SignUpRequestDTO;
import com.alreadyemployee.alreadyemployee.user.entity.CustomUserDetails;
import com.alreadyemployee.alreadyemployee.user.entity.User;
import com.alreadyemployee.alreadyemployee.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;


    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;


    public void signUp(SignUpRequestDTO request){
        if(emailDuplicateCheck(request.getEmail())){
            throw new BusinessException(ErrorCode.USER_EMAIL_DUPLICATE);
        }

        String encodedPassword=passwordEncoder.encode(request.getPassword());

        User user=User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(encodedPassword)
                .build();

        userRepository.save(user);
    }

    public SignInResponseDTO signIn(SignInRequestDTO request){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

        // refreshToken DB 저장
        refreshTokenService.saveOrUpdate(userDetails.getId(), refreshToken, 7);

        return new SignInResponseDTO(accessToken,"Bearer",refreshToken);
    }

    public SignInResponseDTO refreshToken(String refreshToken) {

        // 토큰 유효성 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BusinessException(ErrorCode.USER_INVALID_TOKEN);
        }

        String email = jwtTokenProvider.getUsername(refreshToken);
        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(email);

        // DB의 RefreshToken과 일치하는지 확인
        refreshTokenService.validateRefreshToken(userDetails.getId(), refreshToken);

        String newAccessToken = jwtTokenProvider.generateAccessToken(userDetails);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

        // 새 RefreshToken DB 갱신
        refreshTokenService.updateRefreshToken(userDetails.getId(), newRefreshToken, 7);

        return new SignInResponseDTO(newAccessToken, "Bearer", newRefreshToken);
    }


    public boolean emailDuplicateCheck(String email){
        return userRepository.findByEmail(email).isPresent();
    }

}
