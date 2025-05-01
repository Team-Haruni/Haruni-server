package org.haruni.domain.diary.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(hidden = true)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "diaries")
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "user_id")
    private Long userId;

    @Column(nullable = false, name = "day_summary_description", columnDefinition = "TEXT")
    private String daySummaryDescription;

    @Column(name = "day_summary_image", columnDefinition = "TEXT", nullable = false)
    private String daySummaryImage;

    @Enumerated(EnumType.STRING)
    private Mood mood;

    @Column(nullable = false)
    private String date;

    @Builder
    private Diary(Long userId, String daySummaryDescription, String daySummaryImage, Mood mood, String date) {
        this.userId = userId;
        this.daySummaryDescription = daySummaryDescription;
        this.daySummaryImage = daySummaryImage;
        this.mood = mood;
        this.date = date;
    }
}