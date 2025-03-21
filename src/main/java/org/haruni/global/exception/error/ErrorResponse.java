package org.haruni.global.exception.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(hidden = true)
@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse<T> {
    private final Integer error;
    private final T message;
}
