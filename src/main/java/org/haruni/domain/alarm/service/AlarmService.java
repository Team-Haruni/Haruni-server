package org.haruni.domain.alarm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.haruni.domain.alarm.dto.req.AlarmDto;
import org.haruni.domain.alarm.entity.Alarm;
import org.haruni.domain.alarm.repository.AlarmRepository;
import org.haruni.domain.chat.entity.Chat;
import org.haruni.domain.chat.entity.ChatType;
import org.haruni.domain.chat.repository.ChatRepository;
import org.haruni.domain.chat.service.ChatService;
import org.haruni.domain.chatroom.entity.Chatroom;
import org.haruni.domain.chatroom.service.ChatroomService;
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

    private final ChatroomService chatroomService;
    private final ChatService chatService;

    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;
    private final ChatRepository chatRepository;

    private final FirebaseMessaging firebaseMessaging;

    public void scheduleAlarm(){

        List<Alarm> reservedAlarm = userRepository.findAlarmByAlarmActive();

        alarmRepository.saveAll(reservedAlarm);

        log.info("[AlarmService - scheduleAlarm()] : {} 개의 알람 스케줄링 완료", reservedAlarm.size());
    }

    public void sendScheduledAlarm(){

        String now = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        List<AlarmDto> alarms = alarmRepository.findAllBySendingTime(now).stream()
                .map(AlarmDto::entityToDto)
                .toList();

        alarms.forEach(alarmDto -> {
            User user = userRepository.findByFcmToken(alarmDto.getFcmToken());

            Chatroom chatroom = user.getChatrooms().stream()
                    .filter(userChatroom -> userChatroom.getCreatedAt().equals(chatService.getNow()))
                    .findFirst()
                    .orElseGet(() -> chatroomService.createChatroom(user, chatService.getNow()));

            Chat chat = new Chat(user.getNickname(), chatroom, ChatType.HARUNI, alarmDto.getContent(), chatService.getNow());

            chatroom.getChats().add(chat);
            chatRepository.save(chat);
        });

        alarmRepository.deleteAllBySendingTime(now);

        if(alarms.isEmpty()) return;

        alarms.forEach(alarmDto -> {
            Message message = Message.builder()
                    .setToken(alarmDto.getFcmToken())
                    .putData("content", alarmDto.getContent())
                    .build();

            try{
                String response = firebaseMessaging.send(message);

                log.info("[AlarmService - sendScheduledAlarm()] : {}, 알람 전송 성공", alarmDto.getFcmToken());
            }catch (FirebaseMessagingException e){
                log.error("[AlarmService - sendScheduledAlarm()] : {}, 알람 전송 실패", alarmDto.getFcmToken());
            }
        });
    }

    @Async
    public void sendDayDiaryAlarm(User user){
        Message message = Message.builder()
                .setToken(user.getFcmToken())
                .putData("content", "하루 일기가 생성되었습니다! 확인해보세요!")
                .build();
        try{
            String response = firebaseMessaging.send(message);

            log.info("[AlarmService - sendScheduledAlarm()] : 하루 일기 알람 전송 성공");

            CompletableFuture.completedFuture(true);
        } catch (FirebaseMessagingException e){

            log.error("[AlarmService - sendScheduledAlarm()] : 하루 일기 알람 전송 실패. {}", e.getMessage());

            CompletableFuture.completedFuture(false);
        }
    }

    @Async
    public void updateAlarmSchedule(String fcmToken, String alarmActiveTime){
        try {
            alarmRepository.deleteByFcmToken(fcmToken);

            Alarm alarm = new Alarm(fcmToken, alarmActiveTime);
            alarmRepository.save(alarm);

            log.info("[AlarmService - updateAlarmSchedule()] : 알람 스캐줄링 업데이트 완료");

            CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            log.error("[AlarmService - updateAlarmSchedule()] : 알람 스캐줄링 업데이트 실패. {}", e.getMessage());

            CompletableFuture.completedFuture(false);
        }
    }
}