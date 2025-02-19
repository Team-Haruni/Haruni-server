package org.haruni.domain.diary.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Schema(description = "월별 캘린더 Response")
public class MonthDiaryResponseDto {

    @Schema(
            description = "월",
            example = "12"
    )
    private final String month;

    @Schema(
            description = "특정 달에 하루일기가 생성된 날에 대한 요약 리스트"
    )
    private final List<DayDiarySummaryDto> summaries;

    @Builder
    private MonthDiaryResponseDto(String month, List<DayDiarySummaryDto> summaries) {
        this.month = month;
        this.summaries = summaries;
    }
}
