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

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void scheduleAlarmTask(){
        alarmService.scheduleAlarm();
    }

    @Scheduled(cron = "0 */10 * * * *", zone = "Asia/Seoul")
    public void sendAlarmTask(){
        alarmService.sendScheduledAlarm();
    }

    @Scheduled(cron = "0 07 05 * * *", zone = "Asia/Seoul")
    public void createDayDiary(){ diaryService.createDayDiary(); }

    @Scheduled(cron = "0 00 15 * * SAT", zone = "Asia/Seoul")
    public void createWeekEmotionSummary() { diaryService.createWeekEmotionSummary();}
}