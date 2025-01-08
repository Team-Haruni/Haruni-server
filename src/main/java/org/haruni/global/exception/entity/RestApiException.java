package org.haruni.global.exception.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.haruni.global.exception.error.ErrorCode;

@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException{
    private final ErrorCode errorCode;
}
