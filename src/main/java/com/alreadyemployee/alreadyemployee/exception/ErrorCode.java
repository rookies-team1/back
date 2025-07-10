package com.alreadyemployee.alreadyemployee.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    USER_NOT_FOUND("U001", "해당 사용자를 찾을 수 없습니다. ID: %s", HttpStatus.NOT_FOUND),
    USER_EMAIL_DUPLICATE("U002", "중복된 이메일입니다.", HttpStatus.CONFLICT),
    USER_INVALID_TOKEN("U003", "유효하지 않은 토큰입니다.", HttpStatus.BAD_REQUEST),
    USER_INVALID_EMAIL_VERIFICATION("U004", "인증되지 않은 이메일입니다.", HttpStatus.UNAUTHORIZED),

    NEWS_NOT_FOUND("N001", "해당 뉴스를 찾을 수 없습니다. ID: %s", HttpStatus.NOT_FOUND),

    EXTERNAL_API_ERROR("S002", "외부 요약 서버와 통신 중 오류 발생", HttpStatus.BAD_GATEWAY),

    INVALID_JSON_FORMAT("C001", "JSON 형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    FILE_PROCESSING_ERROR("C002", "파일 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;        // A001, A002 등
    private final String message;     // 사용자에게 보여줄 메시지
    private final HttpStatus status;  //http status 코드

    ErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
