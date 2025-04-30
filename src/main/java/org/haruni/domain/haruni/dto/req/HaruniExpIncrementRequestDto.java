package org.haruni.domain.haruni.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "하루니 레벨 조정 Request")
public class HaruniExpIncrementRequestDto {

    @Schema(
            description = "레벨 조정치",
            example = "5.0"
    )
    @NotNull(message = "레벨 조정치가 비어있습니다.")
    private Double exp;
}
