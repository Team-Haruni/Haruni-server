package org.haruni.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CustomErrorCode implements ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "서버 내부에 오류가 발생했습니다."),

    // Auth
    USER_EMAIL_DUPLICATED(HttpStatus.BAD_REQUEST, 400, "이메일 중복 검사에 실패하였습니다"),
    USER_FCM_TOKEN_DUPLICATED(HttpStatus.BAD_REQUEST, 400, "FCM 토큰 중복 검사에 실패하였습니다"),
    // Haruni
    HARUNI_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "하루니 조회에 실패하였습니다"),
    POST_MESSAGE_TO_MODEL_SERVER_FAILED(HttpStatus.BAD_REQUEST, 400, "하루니 서버로의 채팅 전송에 실패하였습니다"),

    // OAuth 2.0
    OAUTH_NAVER_UNLINK_FAILED(HttpStatus.BAD_REQUEST, 400, "네이버 소셜 계정 연동 해지에 실패하였습니다."),
    OAUTH2_INVALID_PROVIDER(HttpStatus.BAD_REQUEST, 400, "유효하지 않은 소셜 로그인 제공자입니다."),
    OAUTH2_ACCESS_TOKEN_UNAVAILABLE(HttpStatus.UNAUTHORIZED, 401, "유효하지 않은 인증서버의 엑세스 토큰입니다"),

    // Diary
    DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "하루일기 조회에 실패하였습니다."),

    // FCM
    FCM_INITIALIZE_FAILED(HttpStatus.FAILED_DEPENDENCY, 424, "FCM 연결 실패"),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "유저 조회에 실패하였습니다"),

    // Request
    INVALID_PARAMS(HttpStatus.BAD_REQUEST, 400, "유효성 검사에 실패하였습니다");

    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;

}