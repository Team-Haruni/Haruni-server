package org.haruni.domain.feedback.controller.api;

import lombok.RequiredArgsConstructor;
import org.haruni.domain.common.dto.res.ResponseDto;
import org.haruni.domain.feedback.controller.docs.WeeklyFeedbackControllerSpecification;
import org.haruni.domain.feedback.dto.res.WeeklyFeedbackResponseDto;
import org.haruni.domain.feedback.service.WeeklyFeedbackService;
import org.haruni.domain.user.entity.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/weekly-feedback")
public class WeeklyFeedbackController implements WeeklyFeedbackControllerSpecification {

    private final WeeklyFeedbackService weeklyFeedbackService;

    @GetMapping
    public ResponseEntity<ResponseDto<WeeklyFeedbackResponseDto>> getFeedback(@AuthenticationPrincipal UserDetailsImpl user){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(weeklyFeedbackService.getFeedback(user), "주간 사용자 피드백 조회 완료"));
    }
}
