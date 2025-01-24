package org.haruni.domain.diary.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Schema(description = "하루 일기 Response")
public class DayDiaryResponseDto {

    @Schema(
            description = "하루 일기 요약",
            example = "오늘은 한강 잠실교에서 러닝을 뛰었다. 사람이 없어 한적하니 러닝뛰기 딱 좋은 분위기었다. 다음엔 어디로 갈까?"
    )
    private final String description;

    @Schema(
            description = "하루 일기 그림 url",
            example = "https://{bucket-name}.s3.{region}.amazonaws.com/{object-key}"
    )
    private final String daySummaryImgUrl;

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
    private DayDiaryResponseDto(String description, String daySummaryImgUrl, String mood, String date) {
        this.description = description;
        this.daySummaryImgUrl = daySummaryImgUrl;
        this.mood = mood;
        this.date = date;
    }
}