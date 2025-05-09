package org.haruni.domain.diary.dto.req;

import lombok.Builder;
import lombok.Getter;
import org.haruni.domain.diary.entity.Mood;

@Getter
public class DiaryDto {
    private final String date;
    private final Mood mood;
    private final String summary;

    @Builder
    private DiaryDto(String date, Mood mood, String summary) {
        this.date = date;
        this.mood = mood;
        this.summary = summary;
    }
}