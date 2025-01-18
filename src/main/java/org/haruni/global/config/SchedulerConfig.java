package org.haruni.global.config;

import lombok.RequiredArgsConstructor;
import org.haruni.domain.alarm.service.AlarmService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
public class SchedulerConfig {

    private final AlarmService alarmService;

    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleAlarmTask(){
        alarmService.scheduleAlarm();
    }

    @Scheduled(cron = "0 */10 * * * *")
    public void sendAlarmTask(){
        alarmService.sendAlarm();
    }
}
