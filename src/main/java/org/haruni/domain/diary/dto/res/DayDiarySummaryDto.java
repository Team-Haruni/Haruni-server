package org.haruni.domain.diary.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.haruni.domain.diary.entity.Diary;

@Getter
public class DayDiarySummaryDto {

    @Schema(
            description = "날짜",
            example = "25"
    )
    private final String date;

    @Schema(
            description = "기분",
            example = "HAPPY"
    )
    private final String mood;

    @Builder
    private DayDiarySummaryDto(String date, String mood) {
        this.date = date;
        this.mood = mood;
    }

    public static DayDiarySummaryDto entityToDto(Diary diary){
        return DayDiarySummaryDto.builder()
                .date(diary.getDate())
                .mood(diary.getMood().getEmotion())
                .build();
    }
}
