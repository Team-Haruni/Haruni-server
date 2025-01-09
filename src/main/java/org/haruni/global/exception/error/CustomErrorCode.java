package org.haruni.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CustomErrorCode implements ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "서버 내부에 오류가 발생했습니다."),

    // Auth
    USER_EMAIL_DUPLICATED(HttpStatus.BAD_REQUEST, 400, "중복되는 이메일입니다."),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "유저가 존재하지 않습니다."),

    // Request
    INVALID_PARAMS(HttpStatus.BAD_REQUEST, 400, "RequestBody 유효성 검사에 실패하였습니다.");

    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;

}