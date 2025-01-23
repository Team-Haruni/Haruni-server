package org.haruni.global.config;

import lombok.RequiredArgsConstructor;
import org.haruni.domain.alarm.service.AlarmService;
import org.haruni.domain.diary.service.DiaryService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
public class SchedulerConfig {

    private final AlarmService alarmService;
    private final DiaryService diaryService;

    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleAlarmTask(){
        alarmService.scheduleAlarm();
    }

    @Scheduled(cron = "0 */10 * * * *")
    public void sendAlarmTask(){
        alarmService.sendScheduledAlarm();
    }

    @Scheduled(cron = "0 50 23 * * *")
    public void createDayDiary(){
        diaryService.createDayDiary();
    }

}
