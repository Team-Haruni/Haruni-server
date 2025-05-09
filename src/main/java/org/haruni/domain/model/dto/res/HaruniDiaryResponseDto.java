package org.haruni.domain.model.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

// [Model to Spring] 다이어리 생성 요청
@Getter
public class HaruniDiaryResponseDto {

    @Schema(
            description = "하루 일기 요약",
            example = "오늘은 한강 잠실교에서 러닝을 뛰었다. 사람이 없어 한적하니 러닝뛰기 딱 좋은 분위기었다. 다음엔 어디로 갈까?"
    )
    private final String daySummaryDescription;

    @Schema(
            description = "일기 이미지 URL",
            example = "https://haruni-site/test.png"
    )
    private final String daySummaryImage;

    @Schema(
            description = "기분",
            example = "HAPPY"
    )
    private final String mood;

    @Schema(
            description = "날짜",
            example = "2025-01-23"
    )
    private final String date;

    @Builder
    private HaruniDiaryResponseDto(String daySummaryDescription, String daySummaryImage, String mood, String date) {
        this.daySummaryDescription = daySummaryDescription;
        this.daySummaryImage = daySummaryImage;
        this.mood = mood;
        this.date = date;
    }
}
