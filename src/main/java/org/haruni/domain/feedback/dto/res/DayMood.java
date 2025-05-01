package org.haruni.domain.feedback.dto.res;

import lombok.Builder;
import lombok.Getter;
import org.haruni.domain.diary.entity.Mood;

@Getter
public class DayMood {

    private final String date;
    private final Mood mood;

    @Builder
    private DayMood(String date, Mood mood) {
        this.date = date;
        this.mood = mood;
    }
}
