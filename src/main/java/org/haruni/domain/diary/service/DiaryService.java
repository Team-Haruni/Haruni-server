package org.haruni.domain.diary.service;

import lombok.extern.log4j.Log4j2;
import org.haruni.domain.alarm.service.AlarmService;
import org.haruni.domain.chat.dto.req.ChatDto;
import org.haruni.domain.chat.entity.ChatType;
import org.haruni.domain.chat.repository.ChatRepository;
import org.haruni.domain.common.util.TimeUtils;
import org.haruni.domain.model.dto.req.HaruniDiaryRequestDto;
import org.haruni.domain.diary.dto.req.DiaryDto;
import org.haruni.domain.diary.dto.res.DayDiaryResponseDto;
import org.haruni.domain.diary.dto.res.DayDiarySummaryDto;
import org.haruni.domain.model.dto.res.HaruniDiaryResponseDto;
import org.haruni.domain.diary.dto.res.MonthDiaryResponseDto;
import org.haruni.domain.diary.entity.Diary;
import org.haruni.domain.diary.entity.Mood;
import org.haruni.domain.diary.repository.DiaryRepository;
import org.haruni.domain.feedback.dto.res.DayMood;
import org.haruni.domain.model.dto.res.HaruniFeedbackResponseDto;
import org.haruni.domain.feedback.entity.WeeklyFeedback;
import org.haruni.domain.feedback.repository.WeeklyFeedbackRepository;
import org.haruni.domain.user.dto.res.UserSummaryDto;
import org.haruni.domain.user.entity.User;
import org.haruni.domain.user.entity.UserDetailsImpl;
import org.haruni.domain.user.repository.UserRepository;
import org.haruni.global.exception.entity.RestApiException;
import org.haruni.global.exception.error.CustomErrorCode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Log4j2
@Service
public class DiaryService {

    private final AlarmService alarmService;

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final WeeklyFeedbackRepository weeklyFeedbackRepository;

    private final RestTemplate modelServerTemplate;

    public DiaryService(AlarmService alarmService, DiaryRepository diaryRepository, UserRepository userRepository, @Qualifier("modelServerTemplate") RestTemplate modelServerTemplate, ChatRepository chatRepository, WeeklyFeedbackRepository weeklyFeedbackRepository) {
        this.alarmService = alarmService;
        this.diaryRepository = diaryRepository;
        this.userRepository = userRepository;
        this.modelServerTemplate = modelServerTemplate;
        this.chatRepository = chatRepository;
        this.weeklyFeedbackRepository = weeklyFeedbackRepository;
    }

    @Transactional(readOnly = true)
    public DayDiaryResponseDto getDayDiary(UserDetailsImpl authUser, String date){
        User user = userRepository.findById(authUser.getUser().getId())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        Diary diary = diaryRepository.findByUserIdAndDate(user.getId(), date)
                .orElseThrow(() -> new RestApiException(CustomErrorCode.DIARY_NOT_FOUND));

        log.info("getDayDiary() - {}의 {}일 하루 일기 조회 완료", authUser.getUser().getEmail(), date);

        return DayDiaryResponseDto.builder()
                .diary(diary)
                .build();
    }

    @Transactional(readOnly = true)
    public MonthDiaryResponseDto getMonthDiary(UserDetailsImpl authUser, String month){

        User user = userRepository.findByEmail(authUser.getUser().getEmail())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        List<DayDiarySummaryDto> summaries = diaryRepository.findAllByUserAndStartWithMonth(user.getId(), month).stream()
                .map(DayDiarySummaryDto::entityToDto)
                .toList();

        log.info("getMonthDiary() : {}의 {}월 다이어리 조회 성공", authUser.getUser().getEmail(), month);

        return MonthDiaryResponseDto.builder()
                .month(month)
                .summaries(summaries)
                .build();
    }

    @Transactional
    public void createDayDiary(){

        String date = TimeUtils.getCurrentDate();

        List<Long> userIds = chatRepository.findUserIdsByMinChatsOnDate(10L, date);

        if(userIds.isEmpty())
            return;

        List<UserSummaryDto> userSummaries = userRepository.findUserSummariesByUserIds(userIds);

        userSummaries.forEach(userSummary -> {
            List<ChatDto> chats = chatRepository.findAllByUserIdAndSendingDate(userSummary.getUserId(), date, ChatType.USER);

            HaruniDiaryRequestDto request = HaruniDiaryRequestDto.builder()
                    .userSummary(userSummary)
                    .chats(chats)
                    .build();

            try{
                HaruniDiaryResponseDto response = modelServerTemplate.postForObject(
                        "/api/v1/day-diary",
                        request,
                        HaruniDiaryResponseDto.class
                );

                if(response == null){
                    throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
                }

                Diary diary = Diary.builder()
                        .userId(request.getUserId())
                        .daySummaryDescription(response.getDaySummaryDescription())
                        .daySummaryImage(response.getDaySummaryImage())
                        .mood(Mood.fromEmotion(response.getMood()))
                        .date(date)
                        .build();

                diaryRepository.save(diary);

                alarmService.sendDayDiaryAlarm(userSummary.getUserId());

                log.info("createDayDiary() - userId#{}의 하루일기 생성 성공", userSummary.getUserId());
            }catch (HttpClientErrorException e){
                log.error("createDayDiary()- userId#{}의 하루일기 생성 실패", userSummary.getUserId());
            }
        });
    }

    @Transactional
    public void createWeekEmotionSummary(){

        String startDate = TimeUtils.getDateDaysAgo(8L);
        String endDate = TimeUtils.getDateDaysAgo(1L);

        List<Long> userIds = diaryRepository.findUserIdsByDateBetween(startDate, endDate);

        userIds.forEach(userId -> {
            List<DiaryDto> diaries = diaryRepository.findDiariesByDateBetween(userId, startDate, endDate);

            try{
                HaruniFeedbackResponseDto response = modelServerTemplate.postForObject(
                        "/api/v1/week-status",
                        diaries,
                        HaruniFeedbackResponseDto.class
                );

                if(response == null)
                    throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);

                WeeklyFeedback weeklyFeedback = WeeklyFeedback.builder()
                        .userId(userId)
                        .feedback(response.getFeedback())
                        .weekSummary(response.getWeekSummary())
                        .suggestion(response.getSuggestion())
                        .recommendation(response.getRecommendation())
                        .startDate(startDate)
                        .endDate(endDate)
                        .build();

                weeklyFeedbackRepository.save(weeklyFeedback);

                alarmService.sendWeeklyFeedbackAlarm(userId);

                log.info("createWeekEmotionSummary() - userId#{}의 주간 피드백 생성 완료", userId);
            }catch (HttpClientErrorException e){
                log.error("createWeekEmotionSummary() - userId#{}의 주간 피드백 생성 실패", userId);
            }
        });
    }

    public List<DayMood> getDayMoods(Long userId, String startDate, String endDate){
        return diaryRepository.findDayMoodByDateBetween(userId, startDate, endDate);
    }
}