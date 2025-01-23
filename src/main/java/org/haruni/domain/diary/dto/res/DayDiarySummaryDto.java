package org.haruni.domain.diary.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.haruni.domain.diary.entity.Diary;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DayDiarySummaryDto {

    @Schema(description = "날짜", example = "25")
    private String date;

    @Schema(description = "기분", example = "HAPPY")
    private String mood;

    public static DayDiarySummaryDto from(Diary diary){
        return DayDiarySummaryDto.builder()
                .date(diary.getDate())
                .mood(diary.getMood().getEmotion())
                .build();
    }
}
