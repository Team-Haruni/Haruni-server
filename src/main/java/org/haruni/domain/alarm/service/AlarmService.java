package org.haruni.domain.alarm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.haruni.domain.alarm.dto.req.AlarmDto;
import org.haruni.domain.alarm.entity.AlarmContent;
import org.haruni.domain.chat.entity.Chat;
import org.haruni.domain.chat.entity.ChatType;
import org.haruni.domain.chat.repository.ChatRepository;
import org.haruni.domain.chat.service.ChatService;
import org.haruni.domain.chatroom.entity.Chatroom;
import org.haruni.domain.chatroom.service.ChatroomService;
import org.haruni.domain.diary.entity.Diary;
import org.haruni.domain.user.dto.res.UserAlarmDto;
import org.haruni.domain.user.entity.User;
import org.haruni.domain.user.repository.UserRepository;
import org.haruni.global.exception.entity.RestApiException;
import org.haruni.global.exception.error.CustomErrorCode;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    // TODO ALARM_HASH 값 설정 파일에서 주입받는 방식으로 변경
    private static final String ALARM_HASH = "alarm";

    private final ChatroomService chatroomService;
    private final ChatService chatService;

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    private final FirebaseMessaging firebaseMessaging;

    private final RedisTemplate<String, String> redisTemplate;
    private final HashOperations<String, String, String> hashOperations;

    @Transactional
    public void scheduleAlarm() {

        List<UserAlarmDto> userAlarm = userRepository.findAlarmByAlarmActive();

        userAlarm.forEach(alarm -> saveAlarm(alarm.getFcmToken(), alarm.getAlarmActiveTime()));

        log.info("[AlarmService - scheduleAlarm()] : {} 개의 알람 스케줄링 완료", userAlarm.size());
    }

    @Transactional
    public void sendScheduledAlarm() {

        String now = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));

        Map<String, String> entireAlarm = hashOperations.entries(ALARM_HASH);

        List<AlarmDto> alarms = entireAlarm.entrySet().stream()
                .filter(entry -> entry.getValue().equals(now))
                .map(entry -> AlarmDto.builder()
                        .fcmToken(entry.getKey())
                        .content(AlarmContent.getRandomMessage())
                        .build())
                .toList();

        alarms.forEach(alarmDto -> {
            User user = userRepository.findByFcmToken(alarmDto.getFcmToken());

            Chatroom chatroom = user.getChatrooms().stream()
                    .filter(cr -> cr.getCreatedAt().equals(chatService.getNow()))
                    .findFirst()
                    .orElseGet(() -> chatroomService.createChatroom(user, chatService.getNow()));

            Chat chat = Chat.builder()
                        .senderName(user.getHaruniName())
                        .type(ChatType.HARUNI)
                        .content(alarmDto.getContent())
                        .createdAt(chatService.getNow())
                        .build();

            chatroom.getChats().add(chat);
            chatRepository.save(chat);

            hashOperations.delete(ALARM_HASH, user.getFcmToken());
        });

        if (alarms.isEmpty()) {
            log.info("sendScheduledAlarm() - 스케줄링된 알람이 없습니다.");
            return;
        }

        alarms.forEach(alarmDto -> {

            Notification notification = Notification.builder()
                    .setTitle("하루 질문이 도착했습니다!")
                    .setBody(alarmDto.getContent())
                    .build();

            Message message = Message.builder()
                    .setToken(alarmDto.getFcmToken())
                    .setNotification(notification)
                    .build();

            try {
                String response = firebaseMessaging.send(message);
                log.info("[AlarmService - sendScheduledAlarm()] : {}, 알람 전송 성공", alarmDto.getFcmToken());
            } catch (FirebaseMessagingException e) {
                log.error("[AlarmService - sendScheduledAlarm()] : {}, 알람 전송 실패", alarmDto.getFcmToken());
            }
        });
    }

    @Async
    public void sendDayDiaryAlarm(Long userId, Diary diary) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        user.getDiaries().add(diary);
        userRepository.save(user);

        Notification notification = Notification.builder()
                .setTitle("하루 그림 일기가 도착했습니다!")
                .setBody("앱에서 하루 그림 일기를 확인해보세요!")
                .build();

        Message message = Message.builder()
                .setToken(user.getFcmToken())
                .setNotification(notification)
                .build();

        try {
            String response = firebaseMessaging.send(message);
            log.info("[AlarmService - sendDayDiaryAlarm()] : 하루 일기 알람 전송 성공");
            CompletableFuture.completedFuture(true);
        } catch (FirebaseMessagingException e) {
            log.error("[AlarmService - sendDayDiaryAlarm()] : 하루 일기 알람 전송 실패. {}", e.getMessage());
            CompletableFuture.completedFuture(false);
        }
    }

    @Async
    public void updateAlarmSchedule(String fcmToken, String alarmActiveTime) {
        try {
            updateAlarm(fcmToken, alarmActiveTime);

            log.info("[AlarmService - updateAlarmSchedule()] : 알람 스케줄링 업데이트 완료");

            CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            log.error("[AlarmService - updateAlarmSchedule()] : 알람 스케줄링 업데이트 실패. {}", e.getMessage());
            CompletableFuture.completedFuture(false);
        }
    }

    private void saveAlarm(String fcmToken, String alarmActiveTime) {
        hashOperations.put(ALARM_HASH, fcmToken, alarmActiveTime);
    }

    private void updateAlarm(String fcmToken, String updatedAlarmActiveTime) {
        hashOperations.put(ALARM_HASH, fcmToken, updatedAlarmActiveTime);
    }
}