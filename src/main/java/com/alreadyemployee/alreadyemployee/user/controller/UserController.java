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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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

    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "회원가입 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "회원가입 성공 응답",
                                    value = """
                {
                    "success": true,
                    "data": null,
                    "message": "요청이 성공적으로 완료되었습니다."
                }
                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "유효성 검사 실패 (이메일 형식 등)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "이메일 형식 오류",
                                    value = """
                {
                    "success": false,
                    "errorCode": "X001",
                    "errorMessage": "유효한 이메일 형식을 입력해주세요."
                }
                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "중복된 이메일",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "이메일 중복 오류",
                                    value = """
                {
                    "success": false,
                    "errorCode": "U002",
                    "errorMessage": "중복된 이메일입니다."
                }
                """
                            )
                    )
            )
    })
    @PostMapping("/signup")
    @Operation(summary="회원 가입",description = "사용자 이름, 이메일, 비밀번호를 요청 body로 받아 회원 가입을 진행합니다.")


    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequestDTO request){
        userService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/signin")
    @Operation(summary="로그인",description = "이메일, 비밀번호를 요청 body로 받아 로그인을 진행합니다.")

    public ResponseEntity<AccessTokenResponseDTO> signIn(@RequestBody @Valid SignInRequestDTO request, HttpServletResponse response){
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
    @Operation(summary="로그아웃",description = "access token을 Authorization header에 넣어 로그아웃을 진행합니다.  " +
            "\n 우측 상단 Authorize 버튼을 누르고 signin을 통해 발급받은 access token 앞에 Bearer 접두사를 붙여 테스트 해주세요.")

    public ResponseEntity<Void> signOut() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();



        if (!(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new BusinessException(ErrorCode.USER_INVALID_TOKEN);
        }

        refreshTokenService.deleteRefreshToken(userDetails.getId());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    @Operation(summary="토큰 재발급",description = "http only cookie에 저장된 refresh token 값으로 access token을 재발급합니다.")

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
        return ResponseEntity.status(HttpStatus.OK).body(new AccessTokenResponseDTO(newTokens.getAccessToken()));

    }
}
