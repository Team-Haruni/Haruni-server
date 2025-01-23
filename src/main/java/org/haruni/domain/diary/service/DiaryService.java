package org.haruni.domain.diary.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.haruni.domain.diary.dto.res.DayDiaryResponseDto;
import org.haruni.domain.diary.dto.res.DayDiarySummaryDto;
import org.haruni.domain.diary.dto.res.MonthDiaryResponseDto;
import org.haruni.domain.diary.entity.Diary;
import org.haruni.domain.diary.repository.DiaryRepository;
import org.haruni.domain.user.entity.User;
import org.haruni.domain.user.repository.UserRepository;
import org.haruni.global.exception.entity.RestApiException;
import org.haruni.global.exception.error.CustomErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;

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
}