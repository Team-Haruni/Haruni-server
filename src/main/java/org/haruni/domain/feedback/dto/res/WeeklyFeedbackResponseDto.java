package org.haruni.domain.feedback.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.haruni.domain.feedback.entity.WeeklyFeedback;

import java.util.List;

@Getter
@Schema(description = "사용자 피드백 Response")
public class WeeklyFeedbackResponseDto {

    @Schema(
            description = "일별 Mood"
    )
    private final List<DayMood> dayMoods;
    @Schema(
            description = "피드백"
    )
    private final String feedback;

    @Schema(
            description = "주간 요약"
    )
    private final String weekSummary;

    @Schema(
            description = "제안"
    )
    private final String suggestion;

    @Schema(
            description = "추천"
    )
    private final String recommendation;

    @Schema(
            description = "시작일"
    )
    private final String startDate;

    @Schema(
            description = "종료일"
    )
    private final String endDate;

    @Builder
    private WeeklyFeedbackResponseDto(List<DayMood> dayMoods, WeeklyFeedback weeklyFeedback) {
        this.dayMoods = dayMoods;
        this.feedback = weeklyFeedback.getFeedback();
        this.weekSummary = weeklyFeedback.getWeekSummary();
        this.suggestion = weeklyFeedback.getSuggestion();
        this.recommendation = weeklyFeedback.getRecommendation();
        this.startDate = weeklyFeedback.getStartDate();
        this.endDate = weeklyFeedback.getEndDate();
    }
}
