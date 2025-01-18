package org.haruni.domain.alarm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.haruni.domain.alarm.dto.req.AlarmDto;
import org.haruni.domain.alarm.entity.Alarm;
import org.haruni.domain.alarm.repository.AlarmRepository;
import org.haruni.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;
    private final FirebaseMessaging firebaseMessaging;

    public void scheduleAlarm(){
        List<Alarm> reservedAlarm = userRepository.findAlarmByAlarmActive();
        alarmRepository.saveAll(reservedAlarm);
    }

    public void sendAlarm(){
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
                log.info("Alarm send Succeed : {}", response);
            }catch (FirebaseMessagingException e){
                log.error("Alarm send Failed: {}", e.getMessage());
            }
        });
    }
}