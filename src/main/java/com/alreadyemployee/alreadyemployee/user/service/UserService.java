package com.alreadyemployee.alreadyemployee.user.service;

import com.alreadyemployee.alreadyemployee.exception.BusinessException;
import com.alreadyemployee.alreadyemployee.exception.ErrorCode;
import com.alreadyemployee.alreadyemployee.jwt.JwtTokenProvider;
import com.alreadyemployee.alreadyemployee.user.controller.dto.SignInRequestDTO;
import com.alreadyemployee.alreadyemployee.user.controller.dto.SignInResponseDTO;
import com.alreadyemployee.alreadyemployee.user.controller.dto.SignUpRequestDTO;
import com.alreadyemployee.alreadyemployee.user.entity.CustomUserDetails;
import com.alreadyemployee.alreadyemployee.user.entity.EmailVerification;
import com.alreadyemployee.alreadyemployee.user.entity.User;
import com.alreadyemployee.alreadyemployee.user.repository.EmailVerificationRepository;
import com.alreadyemployee.alreadyemployee.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;

    private final RefreshTokenService refreshTokenService;


    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;




    public void signUp(SignUpRequestDTO request){
        if(emailDuplicateCheck(request.getEmail())){
            throw new BusinessException(ErrorCode.USER_EMAIL_DUPLICATE);
        }

        EmailVerification verification = emailVerificationRepository
                .findTopByEmailOrderByIdDesc(request.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_INVALID_EMAIL_VERIFICATION));

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

        return new SignInResponseDTO(accessToken,"Bearer",refreshToken,userDetails.getNickname());
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

        return new SignInResponseDTO(newAccessToken, "Bearer", newRefreshToken,userDetails.getNickname());
    }


    public boolean emailDuplicateCheck(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    private String generateCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000); // 6자리 숫자
    }

    public void sendVerificationCode(String email) {
        String code = generateCode();

        EmailVerification verification = EmailVerification.builder()
                .email(email)
                .code(code)
                .verified(false)
                .expiresAt(LocalDateTime.now().plusMinutes(3))
                .build();

        emailVerificationRepository.save(verification);

        sendEmail(email, code);
    }

    private void sendEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("[이미직원] 이메일 인증 코드입니다.");
        message.setText("인증 코드는 다음과 같습니다:\n\n" + code + "\n\n3분 내에 입력해주세요.");

        mailSender.send(message);
    }

    public boolean verifyCode(String email, String inputCode) {
        EmailVerification verification = emailVerificationRepository.findTopByEmailOrderByIdDesc(email)
                .orElseThrow(() -> new IllegalArgumentException("인증 요청을 찾을 수 없습니다."));

        if (verification.isVerified()) throw new IllegalStateException("이미 인증되었습니다.");
        if (verification.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new IllegalStateException("인증 코드가 만료되었습니다.");
        if (!verification.getCode().equals(inputCode))
            throw new IllegalArgumentException("인증 코드가 올바르지 않습니다.");

        verification.setVerified(true);
        emailVerificationRepository.save(verification);
        return true;
    }

}