package com.alreadyemployee.alreadyemployee.user.controller;


import com.alreadyemployee.alreadyemployee.exception.BusinessException;
import com.alreadyemployee.alreadyemployee.exception.ErrorCode;
import com.alreadyemployee.alreadyemployee.user.controller.dto.AccessTokenResponseDTO;
import com.alreadyemployee.alreadyemployee.user.controller.dto.SignInRequestDTO;
import com.alreadyemployee.alreadyemployee.user.controller.dto.SignInResponseDTO;
import com.alreadyemployee.alreadyemployee.user.controller.dto.SignUpRequestDTO;
import com.alreadyemployee.alreadyemployee.user.entity.CustomUserDetails;
import com.alreadyemployee.alreadyemployee.user.service.RefreshTokenService;
import com.alreadyemployee.alreadyemployee.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;


    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequestDTO request){
        userService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/signin")
    public ResponseEntity<AccessTokenResponseDTO> signIn(@RequestBody SignInRequestDTO request, HttpServletResponse response){
        SignInResponseDTO tokenResponse=userService.signIn(request);

        ResponseCookie refreshTokenCookie= ResponseCookie.from("refreshToken",tokenResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)  // 개발환경 http에서는 false, 운영에서는 true
                .sameSite("None") // 개발에서는 Lax (운영은 None)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        response.setHeader("Set-Cookie",refreshTokenCookie.toString());

        return ResponseEntity.status(HttpStatus.OK).body(new AccessTokenResponseDTO(tokenResponse.getAccessToken()));
    }

    @PostMapping("/signout")
    public ResponseEntity<Void> signOut() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();



        if (!(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new BusinessException(ErrorCode.USER_INVALID_TOKEN);
        }

        refreshTokenService.deleteRefreshToken(userDetails.getId());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponseDTO> refreshToken(
            @CookieValue(value = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response) {

        if (refreshToken == null) {
            throw new BusinessException(ErrorCode.USER_INVALID_TOKEN);
        }

        SignInResponseDTO newTokens = userService.refreshToken(refreshToken);

        // 새로운 RefreshToken으로 쿠키 재설정
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", newTokens.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        response.setHeader("Set-Cookie", refreshTokenCookie.toString());

        // AccessToken만 응답
        return ResponseEntity.ok(new AccessTokenResponseDTO(newTokens.getAccessToken()));

    }
}
