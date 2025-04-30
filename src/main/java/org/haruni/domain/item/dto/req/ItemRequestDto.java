package org.haruni.domain.item.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
    private Long index;
}
