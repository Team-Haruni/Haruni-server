package org.haruni.domain.diary.controller.api;

import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.haruni.domain.common.dto.res.ResponseDto;
import org.haruni.domain.diary.controller.docs.DiaryControllerSpecification;
import org.haruni.domain.diary.dto.res.DayDiaryResponseDto;
import org.haruni.domain.diary.dto.res.MonthDiaryResponseDto;
import org.haruni.domain.diary.service.DiaryService;
import org.haruni.domain.user.entity.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diaries")
public class DiaryController implements DiaryControllerSpecification {

    private final DiaryService diaryService;

    @GetMapping("/day")
    public ResponseEntity<ResponseDto<DayDiaryResponseDto>> getDayDiary(@AuthenticationPrincipal UserDetailsImpl user,
                                                                        @RequestParam
                                                                        @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$",
                                                                                message = "날짜 형식은 YYYY-MM-DD 여야 합니다.")
                                                                        String day){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(diaryService.getDayDiary(user, day), "하루 일기 조회 완료"));
    }

    @GetMapping("/month")
    public ResponseEntity<ResponseDto<MonthDiaryResponseDto>> getMonthDiary(@AuthenticationPrincipal UserDetailsImpl user,
                                                                            @RequestParam
                                                                            @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$",
                                                                                    message = "날짜 형식은 YYYY-MM 이어야 합니다.")
                                                                            String month){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(diaryService.getMonthDiary(user, month), "월별 캘린더 조회 완료"));
    }
}