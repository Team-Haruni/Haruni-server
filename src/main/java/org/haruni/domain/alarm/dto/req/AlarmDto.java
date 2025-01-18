package org.haruni.domain.alarm.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.haruni.domain.alarm.entity.Alarm;
import org.haruni.domain.alarm.entity.AlarmContent;

@Getter
@Builder
@AllArgsConstructor
public class AlarmDto {

    private String fcmToken;
    private String content;

    public static AlarmDto from(Alarm alarm){
        return AlarmDto.builder()
                .fcmToken(alarm.getFcmToken())
                .content(AlarmContent.getRandomMessage())
                .build();
    }

}
