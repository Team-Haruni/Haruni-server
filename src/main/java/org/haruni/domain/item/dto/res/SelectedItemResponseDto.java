package org.haruni.domain.item.dto.res;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "선택된 아이탬 정보 Response")
public class SelectedItemResponseDto {

    @Schema(
            description = "아이탬 종류",
            example = "hat"
    )
    private final String type;

    @Schema(
            description = "아이탬 PK",
            example = "1"
    )
    private final Long index;

    @Builder
    private SelectedItemResponseDto(String type, Long index) {
        this.type = type;
        this.index = index;
    }
}
