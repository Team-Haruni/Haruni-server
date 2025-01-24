package org.haruni.domain.item.dto.res;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.haruni.domain.item.entity.Item;

@Getter
@Schema(description = "선택된 아이탬 정보 Response")
public class SelectedItemResponseDto {

    @Schema(
            description = "아이탬 이미지 url",
            example = "https://{bucket-name}.s3.{region}.amazonaws.com/{object-key}"
    )
    private final String itemImgUrl;

    @Schema(
            description = "선택 여부",
            example = "true"
    )
    private final Boolean selected;

    @Builder
    private SelectedItemResponseDto(String itemImgUrl, Boolean selected) {
        this.itemImgUrl = itemImgUrl;
        this.selected = selected;
    }

    public static SelectedItemResponseDto entityToDto(Item item){
        return SelectedItemResponseDto.builder()
                .itemImgUrl(item.getItemImgUrl())
                .selected(true)
                .build();
    }
}
