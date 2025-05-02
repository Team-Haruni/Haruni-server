package org.haruni.domain.feedback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.haruni.domain.diary.service.DiaryService;
import org.haruni.domain.feedback.dto.res.DayMood;
import org.haruni.domain.feedback.dto.res.WeeklyFeedbackResponseDto;
import org.haruni.domain.feedback.entity.WeeklyFeedback;
import org.haruni.domain.feedback.repository.WeeklyFeedbackRepository;
import org.haruni.domain.user.entity.UserDetailsImpl;
import org.haruni.global.exception.entity.RestApiException;
import org.haruni.global.exception.error.CustomErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class WeeklyFeedbackService {

    private final DiaryService diaryService;

    private final WeeklyFeedbackRepository weeklyFeedbackRepository;
    private final RestClient.Builder builder;

    @Transactional(readOnly = true)
    public WeeklyFeedbackResponseDto getFeedback(UserDetailsImpl authUser){
        WeeklyFeedback weeklyFeedback =  weeklyFeedbackRepository.findTopByUserIdOrderByIdDesc(authUser.getUser().getId())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.FEEDBACK_NOT_CREATED));

        List<DayMood> dayMoods = diaryService.getDayMoods(authUser.getUser().getId(), weeklyFeedback.getStartDate(), weeklyFeedback.getEndDate());

        log.info("getFeedback() - {}의 피드백 조회 완료", authUser.getUser().getEmail());

        return WeeklyFeedbackResponseDto.builder()
                .dayMoods(dayMoods)
                .weeklyFeedback(weeklyFeedback)
                .build();
    }
}
