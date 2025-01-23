package org.haruni.domain.diary.service;

import lombok.extern.slf4j.Slf4j;
import org.haruni.domain.alarm.service.AlarmService;
import org.haruni.domain.chatroom.entity.Chatroom;
import org.haruni.domain.chatroom.repository.ChatroomRepository;
import org.haruni.domain.diary.dto.res.DayDiaryResponseDto;
import org.haruni.domain.diary.dto.res.DayDiarySummaryDto;
import org.haruni.domain.diary.dto.res.MonthDiaryResponseDto;
import org.haruni.domain.diary.entity.Diary;
import org.haruni.domain.diary.repository.DiaryRepository;
import org.haruni.domain.user.entity.User;
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
    public DayDiaryResponseDto getDayDiary(User authUser, String date){
        log.info("[DiaryService - getDayDiary()] : {}의 다이어리 조회 시작", date);

        User user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        Diary diary = diaryRepository.findByUserIdAndDate(user.getId(), date)
                .orElseThrow(() -> new RestApiException(CustomErrorCode.DIARY_NOT_FOUND));

        log.info("[DiaryService - getDayDiary()] : {}의 다이어리 조회 완료", date);

        return DayDiaryResponseDto.builder()
                .description(diary.getDescription())
                .daySummaryImgUrl(diary.getS3ImgUrl())
                .mood(diary.getMood().getEmotion())
                .date(diary.getDate())
                .build();
    }

    @Transactional(readOnly = true)
    public MonthDiaryResponseDto getMonthDiary(User authUser, String month){
        log.info("[DiaryService - getMonthDiary()] : {}월의 다이어리 조회 시작", month);

        User user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        List<DayDiarySummaryDto> summaries = diaryRepository.findAllByUserAndStartWithMonth(user.getId(), month).stream()
                .map(DayDiarySummaryDto::from)
                .toList();

        log.info("[DiaryService - getMonthDiary()] : {}월의 다이어리 조회 완료", month);
        return MonthDiaryResponseDto.builder()
                .month(month)
                .summaries(summaries)
                .build();
    }

    @Transactional
    public void createDayDiary(){
        log.info("[DiaryService - createDayDiary()] : 하루 일기 생성 시작");

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        List<User> userEmails = chatroomRepository.findByCreatedAt(date).stream()
                .filter(chatroom -> chatroom.getChats().size() >= 6)
                .map(Chatroom::getUser).distinct()
                .toList();

        log.info("[DiaryService - createDayDiary()] : 사용자 이메일 {}개 조회 완료", userEmails.size());

        userEmails.forEach(user -> {
            try {
                DayDiaryResponseDto response = modelServerTemplate.getForObject(
                        "/day-diary/{email}",
                        DayDiaryResponseDto.class,
                        user.getEmail()
                );

                log.info("[DiaryService - createDayDiary()] : [{}의 하루일기 생성 완료]]", user.getNickname());

                Diary diary = Diary.builder()
                        .response(response)
                        .date(date)
                        .user(user)
                        .build();

                diaryRepository.save(diary);
                user.getDiaries().add(diary);

                log.info("[DiaryService - createDayDiary()] : [{}의 하루일기 저장 완료]]", user.getNickname());

                alarmService.sendDayDiaryAlarm(user);

                log.info("[DiaryService - createDayDiary()] : [{}의 하루일기 알림 전송 완료]]", user.getNickname());

            } catch(HttpClientErrorException e) {
                log.error("[DiaryService - createDayDiary()] : [{}의 하루일기 생성 실패]]", user.getNickname());
            }
        });
    }
}