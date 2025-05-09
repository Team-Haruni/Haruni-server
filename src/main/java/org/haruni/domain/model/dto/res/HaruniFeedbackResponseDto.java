package org.haruni.domain.model.dto.res;

import lombok.Builder;
import lombok.Getter;

// [Model to Spring] 피드백 생성
@Getter
public class HaruniFeedbackResponseDto {
    private final String feedback;
    private final String weekSummary;
    private final String suggestion;
    private final String recommendation;

    @Builder
    private HaruniFeedbackResponseDto(String feedback, String weekSummary, String suggestion, String recommendation) {
        this.feedback = feedback;
        this.weekSummary = weekSummary;
        this.suggestion = suggestion;
        this.recommendation = recommendation;
    }
}
