package org.haruni.domain.alarm.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Builder
@Getter
@NoArgsConstructor
@RedisHash(value = "alarm")
public class Alarm {
    @Id
    private String fcmToken;

    private String sendingTime;

    public Alarm(String fcmToken, String sendingTime){
        this.fcmToken = fcmToken;
        this.sendingTime = sendingTime;
    }
}