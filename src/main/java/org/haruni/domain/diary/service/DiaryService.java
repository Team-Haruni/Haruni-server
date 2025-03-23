package org.haruni.domain.diary.service;

import lombok.extern.slf4j.Slf4j;
import org.haruni.domain.alarm.service.AlarmService;
import org.haruni.domain.chat.entity.Chat;
import org.haruni.domain.chatroom.entity.Chatroom;
import org.haruni.domain.chatroom.repository.ChatroomRepository;
import org.haruni.domain.diary.dto.req.DayDiaryRequestDto;
import org.haruni.domain.diary.dto.res.DayDiaryResponseDto;
import org.haruni.domain.diary.dto.res.DayDiarySummaryDto;
import org.haruni.domain.diary.dto.res.MonthDiaryResponseDto;
import org.haruni.domain.diary.entity.Diary;
import org.haruni.domain.diary.repository.DiaryRepository;
import org.haruni.domain.user.dto.req.UserSummaryDto;
import org.haruni.domain.user.entity.User;
import org.haruni.domain.user.entity.UserDetailsImpl;
import org.haruni.domain.user.repository.UserRepository;
import org.haruni.global.exception.entity.RestApiException;
import org.haruni.global.exception.error.CustomErrorCode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DiaryService {

    private final AlarmService alarmService;

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final ChatroomRepository chatroomRepository;

    private final RestTemplate modelServerTemplate;

    public DiaryService(AlarmService alarmService, DiaryRepository diaryRepository, UserRepository userRepository, ChatroomRepository chatroomRepository, @Qualifier("modelServerTemplate") RestTemplate modelServerTemplate) {
        this.alarmService = alarmService;
        this.diaryRepository = diaryRepository;
        this.userRepository = userRepository;
        this.chatroomRepository = chatroomRepository;
        this.modelServerTemplate = modelServerTemplate;
    }

    @Transactional(readOnly = true)
    public DayDiaryResponseDto getDayDiary(UserDetailsImpl authUser, String date){

        User user = userRepository.findByEmail(authUser.getUser().getEmail())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        Diary diary = user.getDiaries().stream()
                        .filter(d -> d.getDate().equals(date))
                        .findFirst()
                        .orElseThrow(() -> new RestApiException(CustomErrorCode.DIARY_NOT_FOUND));

        log.info("getDayDiary() : {} 다이어리 조회 성공", date);

        return DayDiaryResponseDto.builder()
                .description(diary.getDescription())
                .objectKey(diary.getObjectKey())
                .mood(diary.getMood().getEmotion())
                .date(diary.getDate())
                .build();
    }

    @Transactional(readOnly = true)
    public MonthDiaryResponseDto getMonthDiary(UserDetailsImpl authUser, String month){

        User user = userRepository.findByEmail(authUser.getUser().getEmail())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        List<DayDiarySummaryDto> summaries = diaryRepository.findAllByUserAndStartWithMonth(user.getId(), month).stream()
                .map(DayDiarySummaryDto::entityToDto)
                .toList();

        log.info("getMonthDiary() : {}월 다이어리 조회 성공", month);

        return MonthDiaryResponseDto.builder()
                .month(month)
                .summaries(summaries)
                .build();
    }

    @Transactional
    public void createDayDiary(){

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        List<Long> userIds = chatroomRepository.findUserIdsByChatCountAndCreatedAt(6L, date);

        log.info("createDayDiary() : 사용자 이메일 {}개 조회 완료", userIds.size());

        if(userIds.isEmpty())
            return;

        List<UserSummaryDto> userSummaries = userRepository.findUserSummariesByUserIds(userIds);

        List<Chatroom> chatrooms = chatroomRepository.findChatroomByUserIdsAndCreatedAt(userIds, date);

        // <사용자 아이디, 채팅 내용> 구조로 맵 생성
        Map<Long, List<Chat>> userChats = chatrooms.stream()
                        .collect(Collectors.toMap(Chatroom::getUserId, Chatroom::getChats));

        userSummaries.forEach(userSummary -> {
            try{
                List<Chat> chats = userChats.get(userSummary.getUserId());

                DayDiaryRequestDto request = DayDiaryRequestDto.builder()
                        .userSummary(userSummary)
                        .chats(chats)
                        .build();

                DayDiaryResponseDto response = modelServerTemplate.postForObject(
                        "/api/v1/diary",
                        request,
                        DayDiaryResponseDto.class
                );

                log.info("createDayDiary() : userId = {}의 하루일기 생성 성공]", userSummary.getUserId());

                Diary diary= Diary.builder()
                        .response(response)
                        .date(date)
                        .build();

                diaryRepository.save(diary);

                alarmService.sendDayDiaryAlarm(userSummary.getUserId());
            }catch(HttpClientErrorException e) {
                log.error("createDayDiary() : userId = {}의 하루일기 생성 실패]", userSummary.getUserId());
            }
        });
    }
}