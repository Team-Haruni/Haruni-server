package org.haruni.domain.diary.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "하루 일기 Response")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DayDiaryResponseDto {

    @Schema(description = "하루 일기 요약", example = "오늘은 제주도 명물 돔배고기를 먹으러 갔다. 기대했던 그 맛이었다. 너무 행복했다!")
    private String description;

    @Schema(description = "하루 일기 그림 url", example = "https://{bucket-name}.s3.{region}.amazonaws.com/{object-key}")
    private String daySummaryImgUrl;

    @Schema(description = "하루의 분위기", example = "HAPPY")
    private String mood;

    @Schema(description = "날짜", example = "2025-01-23")
    private String date;
}
