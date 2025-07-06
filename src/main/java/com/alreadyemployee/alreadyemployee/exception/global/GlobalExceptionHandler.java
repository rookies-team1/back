package com.alreadyemployee.alreadyemployee.exception.global;

import com.alreadyemployee.alreadyemployee.exception.BusinessException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
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

    /**
     * ✅ 모든 정상 응답을 감싸주는 ResponseBodyAdvice
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true; // 모든 Controller 응답에 적용
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  org.springframework.http.MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  org.springframework.http.server.ServerHttpRequest request,
                                  org.springframework.http.server.ServerHttpResponse response) {
        // 예외 응답은 그대로 return
        if (body instanceof ErrorResponse) return body;

        return new SuccessResponse<>(
                true,
                body,
                "요청이 성공적으로 완료되었습니다."
        );
    }
}
