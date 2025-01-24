package org.haruni.domain.item.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "아이탬 Request")
public class ItemRequestDto {

    @Schema(
            description = "아이탬 이미지 url",
            example = "https://{bucket-name}.s3.{region}.amazonaws.com/{object-key}"
    )
    @NotBlank(message = "아이탬 이미지 url은 공백이 될 수 없습니다.")
    private String itemImgUrl;

    @Schema(
            description = "활성화 래밸",
            example = "3"
    )
    @NotNull(message = "활성화 래밸은 공백이 될 수 없습니다.")
    @Max(value = 5, message = "최대 활성화 래밸은 5입니다.")
    @Min(value = 1, message = "최소 활성화 래밸은 1입니다.")
    private Double activeLevel;

    // TODO 좌표 범위 값 실제 값으로 교체하기

    @Schema(
            description = "아이탬 배치 x 좌표",
            example = "4.2"
    )
    @NotNull(message = "아이탬 배치 x 좌표는 공백이 될 수 없습니다.")
    @Max(value = 100, message = "아이탬 배치 x 좌표의 최대값은 100입니다.")
    @Min(value = 0, message = "아이탬 배치 x 좌표의 최소값은 0입니다.")
    private Double xPosition;

    @Schema(
            description = "아이탬 배치 y 좌표",
            example = "10.2"
    )
    @NotNull(message = "아이탬 배치 y 좌표는 공백이 될 수 없습니다.")
    @Max(value = 100, message = "아이탬 배치 y 좌표의 최대값은 100입니다.")
    @Min(value = 0, message = "아이탬 배치 y 좌표의 최소값은 0입니다.")
    private Double yPosition;
}
