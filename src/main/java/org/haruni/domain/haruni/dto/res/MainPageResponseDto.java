package org.haruni.domain.haruni.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.haruni.domain.item.entity.Item;

import java.util.List;

@Getter
@Schema(description = "메인 패이지 Response")
public class MainPageResponseDto {

    @Schema(
            description = "하루니 이미지 url",
            example = "https://{bucket-name}.s3.{region}.amazonaws.com/{object-key}"
    )
    private final String haruniImgUrl;

    @Schema(
            description = "하루니 레벨(정수부)",
            example = "1"
    )
    private final Integer haruniLevelInteger;

    @Schema(
            description = "하루니 레벨(소수부)",
            example = "0.5"
    )
    private final Double haruniLevelDecimal;

    @Schema(
            description = "인사말",
            example = "삼준서님. 좋은 아침입니다!"
    )
    private final String greetingMessage;

    @Schema(
            description = "배경 이미지 url"
    )
    private final String backgroundImgUrl;

    @Schema(
            description = "아이탬 리스트"
    )
    private final List<Item> selectedItems;

    @Builder
    private MainPageResponseDto(String haruniImgUrl, Integer haruniLevelInteger, Double haruniLevelDecimal, String greetingMessage, String backgroundImgUrl, List<Item> selectedItems) {
        this.haruniImgUrl = haruniImgUrl;
        this.haruniLevelInteger = haruniLevelInteger;
        this.haruniLevelDecimal = haruniLevelDecimal;
        this.greetingMessage = greetingMessage;
        this.backgroundImgUrl = backgroundImgUrl;
        this.selectedItems = selectedItems;
    }
}