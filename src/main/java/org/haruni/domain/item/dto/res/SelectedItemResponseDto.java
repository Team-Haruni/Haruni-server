package org.haruni.domain.item.dto.res;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.haruni.domain.item.entity.Item;

@Builder
@Schema(description = "선택된 아이탬 정보 Response")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SelectedItemResponseDto {

    @Schema(description = "아이탬 이미지 url", example = "https://")
    private String itemImgUrl;

    @Schema(description = "선택 여부", example = "true")
    private Boolean selected;

    public static SelectedItemResponseDto from(Item item){
        return SelectedItemResponseDto.builder()
                .itemImgUrl(item.getItemImgUrl())
                .selected(true)
                .build();
    }

}
