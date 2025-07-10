package com.alreadyemployee.alreadyemployee.exception.global;

import com.alreadyemployee.alreadyemployee.exception.BusinessException;

import com.alreadyemployee.alreadyemployee.news.controller.NewsController;
import com.alreadyemployee.alreadyemployee.user.controller.UserController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(annotations = {RestController.class}, basePackageClasses = {UserController.class, NewsController.class})

public class GlobalExceptionHandler implements ResponseBodyAdvice<Object> {

    private final HttpServletRequest request;

    /**
     * ✅ BusinessException 처리
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        log.warn("BusinessException 발생: {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                false,
                ex.getErrorCode().getCode(),
                ex.getErrorCode().getMessage()
        );

        return ResponseEntity
                .status(ex.getErrorCode().getStatus())
                .body(response);
    }

    /**
     * ✅ 예상하지 못한 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("Unhandled Exception", ex);

        ErrorResponse response = new ErrorResponse(
                false,
                "X999",
                "서버 내부 오류가 발생했습니다."
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        // 첫 번째 필드 에러 메시지만 추출
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();

        ErrorResponse response = new ErrorResponse(
                false,
                "X001",  // 커스텀 코드 (필요시 변경 가능)
                errorMessage
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialException(BadCredentialsException ex){
        ErrorResponse response=new ErrorResponse(
                false,
                "U004",
                "올바르지 않은 사용자 정보입니다."
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * ✅ 모든 정상 응답을 감싸주는 ResponseBodyAdvice
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        String requestURI = request.getRequestURI();
        // Swagger 관련 요청은 제외
        return !requestURI.startsWith("/v3/api-docs")
                && !requestURI.startsWith("/swagger")
                && !requestURI.startsWith("/swagger-ui")
                && !requestURI.startsWith("/favicon")
                && !requestURI.contains("springdoc");
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        if (body instanceof ErrorResponse) return body;

        // 🔥 String 타입은 SuccessResponse로 감싸면 안 됨 → 충돌 발생
        if (body instanceof String) {
            // SuccessResponse를 JSON 문자열로 수동 변환
            SuccessResponse<String> wrapper = new SuccessResponse<>(true, (String) body, "요청이 성공적으로 완료되었습니다.");
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.writeValueAsString(wrapper);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("JSON 변환 실패", e);
            }
        }

        return new SuccessResponse<>(true, body, "요청이 성공적으로 완료되었습니다.");
    }
}
