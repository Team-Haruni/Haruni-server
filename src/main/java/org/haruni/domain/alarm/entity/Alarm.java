package org.haruni.domain.alarm.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "alarm")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm {
    @Id
    private String fcmToken;

    private String sendingTime;

    @Builder
    public Alarm(String fcmToken, String sendingTime){
        this.fcmToken = fcmToken;
        this.sendingTime = sendingTime;
    }
}