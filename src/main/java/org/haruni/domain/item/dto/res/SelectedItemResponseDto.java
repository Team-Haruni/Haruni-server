package org.haruni.domain.item.dto.res;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.haruni.domain.item.entity.Item;

@Getter
@Schema(description = "선택된 아이탬 정보 Response")
public class SelectedItemResponseDto {

    @Schema(
            description = "아이탬 PK",
            example = "1"
    )
    private final Long index;

    @Builder
    private SelectedItemResponseDto(Long index) {
        this.index = index;
    }

    public static SelectedItemResponseDto entityToDto(Item item){
        return SelectedItemResponseDto.builder()
                .index(item.getItemIndex())
                .build();
    }
}
