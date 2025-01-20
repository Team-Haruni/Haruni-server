package org.haruni.domain.item.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "아이탬 선택 Request")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemSaveRequestDto {

    @Schema(description = "저장할 아이탬 리스트")
    @Size(max = 3, message = "아이템은 최대 3개까지만 저장 가능합니다")
    private List<ItemRequestDto> items;
}
