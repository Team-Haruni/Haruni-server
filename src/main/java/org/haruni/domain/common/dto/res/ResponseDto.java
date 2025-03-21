package org.haruni.domain.common.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(hidden = true)
@Getter
public class ResponseDto<T> {
    private final T data;
    private final String message;

    @Builder
    private ResponseDto(T data, String message){
        this.data = data;
        this.message = message;
    }

    public static <T> ResponseDto<T> of(T data, String message){
        return ResponseDto.<T>builder()
                .data(data)
                .message(message)
                .build();
    }
}
