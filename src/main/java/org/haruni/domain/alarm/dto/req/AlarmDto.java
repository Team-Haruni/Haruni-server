package org.haruni.domain.alarm.dto.req;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AlarmDto {

    private final String fcmToken;
    private final String content;

    @Builder
    private AlarmDto(String fcmToken, String content) {
        this.fcmToken = fcmToken;
        this.content = content;
    }
}