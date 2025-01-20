package org.haruni.domain.haruni.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.haruni.domain.item.entity.Item;

import java.util.List;


@Schema(description = "메인 패이지 Response")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MainPageResponseDto {

    @Schema(description = "하루니 이미지 url")
    private String haruniImgUrl;

    @Schema(description = "하루니 레벨(정수부)", example = "1")
    private Integer haruniLevelInteger;

    @Schema(description = "하루니 레벨(소수부)", example = "31.5")
    private Double haruniLevelDecimal;

    @Schema(description = "인사말", example = "삼준서님. 좋은 아침입니다!")
    private String greetingMessage;

    @Schema(description = "배경 이미지 url")
    private String backgroundImgUrl;

    @Schema(description = "아이탬 리스트")
    private List<Item> selectedItems;

}
