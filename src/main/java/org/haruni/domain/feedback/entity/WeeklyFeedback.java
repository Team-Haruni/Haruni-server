package org.haruni.domain.feedback.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "weekly_feedback")
public class WeeklyFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "user_id")
    private Long userId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String feedback;

    @Column(nullable = false, columnDefinition = "TEXT", name = "week_summary")
    private String weekSummary;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String suggestion;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String recommendation;

    @Column(nullable = false, name = "start_date")
    private String startDate;

    @Column(nullable = false, name = "end_date")
    private String endDate;

    @Builder
    private WeeklyFeedback(Long userId, String feedback, String weekSummary,
                           String suggestion, String recommendation, String startDate, String endDate) {
        this.userId = userId;
        this.feedback = feedback;
        this.weekSummary = weekSummary;
        this.suggestion = suggestion;
        this.recommendation = recommendation;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
