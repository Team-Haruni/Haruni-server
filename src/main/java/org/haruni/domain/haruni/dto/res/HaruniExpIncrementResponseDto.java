package org.haruni.domain.haruni.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "하루니 레벨 조정 Response")
public class HaruniExpIncrementResponseDto {

    @Schema(
            description = "하루니 레벨(정수부)",
            example = "1"
    )
    private final Double haruniLevelInteger;

    @Schema(
            description = "하루니 레벨(소수부) = 백분율로 환산된값이라, 그냥 넣으면 됨!",
            example = "0.5"
    )
    private final Double haruniLevelDecimal;

    @Builder
    private HaruniExpIncrementResponseDto(Double haruniLevelInteger, Double haruniLevelDecimal){
        this.haruniLevelInteger = haruniLevelInteger;
        this.haruniLevelDecimal = haruniLevelDecimal;
    }
}
