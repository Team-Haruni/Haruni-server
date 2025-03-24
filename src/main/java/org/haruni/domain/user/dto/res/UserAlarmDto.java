package org.haruni.domain.user.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserAlarmDto {

    private final String fcmToken;

    private final String alarmActiveTime;

    @Builder
    public UserAlarmDto(String fcmToken, String alarmActiveTime){
        this.fcmToken = fcmToken;
        this.alarmActiveTime = alarmActiveTime;
    }
}
