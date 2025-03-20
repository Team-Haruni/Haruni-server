package org.haruni.domain.diary.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import org.haruni.domain.common.dto.res.ResponseDto;
import org.haruni.domain.diary.dto.res.DayDiaryResponseDto;
import org.haruni.domain.diary.dto.res.MonthDiaryResponseDto;
import org.haruni.domain.user.entity.UserDetailsImpl;
import org.haruni.global.exception.error.ErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "DiaryController", description = "Diary management Controller")
public interface DiaryControllerSpecification {

    @Operation(summary = "일별 하루일기 조회", description = "특정 일의 하루일기를 조회합니다<br>" +
                                                         "🔐 <strong>Jwt 필요</strong><br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 일별 하루일기 조회 성공"),
            @ApiResponse(responseCode = "400", description = "🚨 일별 하루일기 조회 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유효성 검사 실패",
                                            value = "{\"error\" : \"400\", \"message\" : \"유효성 검사에 실패하였습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "🚨 일별 하루일기 조회 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유저 조회 실패",
                                            value = "{\"error\" : \"404\", \"message\" : \"유저 조회에 실패하였습니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "하루일기 조회 실패",
                                            value = "{\"error\" : \"404\", \"message\" : \"해당 날짜의 하루일기 조회에 실패하였습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/day")
    ResponseEntity<ResponseDto<DayDiaryResponseDto>> getDayDiary(@AuthenticationPrincipal UserDetailsImpl user,
                                                                 @RequestParam
                                                                 @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$",
                                                                         message = "날짜 형식은 YYYY-MM-DD 여야 합니다.")
                                                                 String day);

    @Operation(summary = "월별 하루일기 조회", description = "특정 달의 하루일기를 조회합니다<br>" +
                                                     "🔐 <strong>Jwt 필요</strong><br>" +
                                                     "📦 <strong>조회된 하루일기가 없을 경우 빈 배열 반환</strong><br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 월별 하루일기 조회 성공"),
            @ApiResponse(responseCode = "400", description = "🚨 월별 하루일기 조회 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유효성 검사 실패",
                                            value = "{\"error\" : \"400\", \"message\" : \"유효성 검사에 실패하였습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "🚨 월별 하루일기 조회 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유저 조회 실패",
                                            value = "{\"error\" : \"404\", \"message\" : \"유저 조회에 실패하였습니다\"}"
                                    ),
                            },
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/month")
    ResponseEntity<ResponseDto<MonthDiaryResponseDto>> getMonthDiary(@AuthenticationPrincipal UserDetailsImpl user,
                                                                     @RequestParam
                                                                     @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$",
                                                                             message = "날짜 형식은 YYYY-MM 이어야 합니다.")
                                                                     String month);
}