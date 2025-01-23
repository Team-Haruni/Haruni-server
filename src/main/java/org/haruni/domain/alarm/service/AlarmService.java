package org.haruni.domain.alarm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.haruni.domain.alarm.dto.req.AlarmDto;
import org.haruni.domain.alarm.entity.Alarm;
import org.haruni.domain.alarm.repository.AlarmRepository;
import org.haruni.domain.user.entity.User;
import org.haruni.domain.user.repository.UserRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;

    private final FirebaseMessaging firebaseMessaging;

    public void scheduleAlarm(){
        log.info("[AlarmService - scheduleAlarm() - 알람 스케줄링 시작]");

        List<Alarm> reservedAlarm = userRepository.findAlarmByAlarmActive();
        alarmRepository.saveAll(reservedAlarm);

        log.info("[AlarmService - scheduleAlarm() - {}개의 알림 저장 완료]", reservedAlarm.size());
        log.info("[AlarmService - scheduleAlarm() - 알람 스케줄링 종료]");
    }

    public void sendScheduledAlarm(){
        log.info("[AlarmService - sendAlarm() - 알람 전송 시작]");

        String now = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        List<AlarmDto> alarms = alarmRepository.findAllBySendingTime(now).stream()
                .map(AlarmDto::from)
                .toList();

        alarmRepository.deleteAllBySendingTime(now);

        if(alarms.isEmpty()) return;

        alarms.forEach(alarmDto -> {
            Message message = Message.builder()
                    .setToken(alarmDto.getFcmToken())
                    .putData("content", alarmDto.getContent())
                    .build();

            try{
                String response = firebaseMessaging.send(message);
                log.info("[AlarmService - sendAlarm() - 알람 전송 성공 {}", alarmDto.getFcmToken());
            }catch (FirebaseMessagingException e){
                log.error("[AlarmService - sendAlarm() - 알람 전송 실패 {}", alarmDto.getFcmToken());
                log.error("[AlarmService - sendAlarm() - Alarm send Failed with {}", e.getMessage());
            }
        });

        log.info("[AlarmService - sendAlarm() - 알람 전송 종료]");
    }

    @Async
    public void sendDayDiaryAlarm(User user){
        Message message = Message.builder()
                .setToken(user.getFcmToken())
                .putData("content", "하루 일기가 생성되었습니다! 확인해보세요!")
                .build();
        try{
            String response = firebaseMessaging.send(message);

            log.info("[AlarmService - sendDayDiaryAlarm() - 알람 전송 성공 {}", user.getFcmToken());

            CompletableFuture.completedFuture(true);
        } catch (FirebaseMessagingException e){
            log.error("[AlarmService - sendDayDiaryAlarm() - 알람 전송 실패 {}", user.getFcmToken());
            log.error("[AlarmService - sendDayDiaryAlarm() - Alarm send Failed with {}", e.getMessage());

            CompletableFuture.completedFuture(false);
        }
    }

    @Async
    public void updateAlarmSchedule(String fcmToken, String alarmActiveTime){
        try {
            Alarm alarm = new Alarm(fcmToken, alarmActiveTime);
            alarmRepository.save(alarm);
            CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            log.error("[AlarmService - updateAlarmSchedule()] - updateAlarmSchedule Failed with {}", e.getMessage());
            CompletableFuture.completedFuture(false);
        }
    }
}