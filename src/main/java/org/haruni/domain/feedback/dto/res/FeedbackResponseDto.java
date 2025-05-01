package org.haruni.domain.feedback.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FeedbackResponseDto {
    private final String feedback;
    private final String weekSummary;
    private final String suggestion;
    private final String recommendation;

    @Builder
    private FeedbackResponseDto(String feedback, String weekSummary, String suggestion, String recommendation) {
        this.feedback = feedback;
        this.weekSummary = weekSummary;
        this.suggestion = suggestion;
        this.recommendation = recommendation;
    }
}
