package org.haruni.domain.item.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
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
            description = "아이탬 종류",
            example = "hat"
    )
    @NotBlank(message = "아이탬 종류는 공백이 될 수 없습니다.")
    private String itemType;


    @Schema(
            description = "아이탬 인덱스",
            example = "1"
    )
    @NotNull(message = "아이탬 인덱스는 공백이 될 수 없습니다.")
    private Long itemIndex;
}
