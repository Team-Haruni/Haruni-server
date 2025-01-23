package org.haruni.domain.diary.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Schema(description = "월별 캘린더 Response")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MonthDiaryResponseDto {

    @Schema(description = "월", example = "12")
    private String month;

    @Schema(description = "월에 하루 일기가 생성된 날의 요약")
    private List<DayDiarySummaryDto> summaries;
}
