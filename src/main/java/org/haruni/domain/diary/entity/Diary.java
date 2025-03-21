package org.haruni.domain.diary.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.haruni.domain.diary.dto.res.DayDiaryResponseDto;

@Schema(hidden = true)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "diaries")
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "object_key")
    private String objectKey;

    @Enumerated(EnumType.STRING)
    private Mood mood;

    @Column(nullable = false)
    private String date;

    @Builder
    public Diary(DayDiaryResponseDto response, String date) {
        this.description = response.getDescription();
        this.objectKey = response.getObjectKey();
        this.mood = Mood.fromEmotion(response.getMood());
        this.date = date;
    }
}