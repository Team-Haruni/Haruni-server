package org.haruni.domain.alarm.dto.req;

import lombok.Builder;
import lombok.Getter;
import org.haruni.domain.alarm.entity.Alarm;
import org.haruni.domain.alarm.entity.AlarmContent;

@Getter
public class AlarmDto {

    private final String fcmToken;
    private final String content;

    @Builder
    private AlarmDto(String fcmToken, String content) {
        this.fcmToken = fcmToken;
        this.content = content;
    }

    public static AlarmDto entityToDto(Alarm alarm){
        return AlarmDto.builder()
                .fcmToken(alarm.getFcmToken())
                .content(AlarmContent.getRandomMessage())
                .build();
    }
}