package org.haruni.domain.user.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.haruni.domain.common.dto.res.ResponseDto;
import org.haruni.domain.user.dto.req.AlarmActiveTimeUpdateRequestDto;
import org.haruni.domain.user.dto.req.EmailUpdateRequestDto;
import org.haruni.domain.user.dto.res.UserInfoResponseDto;
import org.haruni.domain.user.entity.User;
import org.haruni.global.exception.error.ErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "UserController", description = "User management Controller")
public interface UserControllerSpecification {

    @Operation(summary = "사용자 정보 로드", description = "사용자 정보를 불러옵니다<br>" +
                                                          "🔐 <strong>Jwt 필요</strong><br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 사용자 정보 조회 성공")
    })
    @GetMapping
    ResponseEntity<ResponseDto<UserInfoResponseDto>> getUserInfo(@AuthenticationPrincipal User user);

    @Operation(summary = "사용자 이메일 수정", description = "사용자의 이메일을 수정합니다<br>" +
                                                            "🔐 <strong>Jwt 필요</strong><br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 사용자 이메일 수정 성공"),
            @ApiResponse(responseCode = "400", description = "🚨 사용자 이메일 수정 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유효성 검사 실패",
                                            value = "{\"error\" : \"400\", \"message\" : \"유효성 검사에 실패하였습니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "이메일 중복",
                                            value = "{\"error\" : \"400\", \"message\" : \"이메일 중복 검사에 실패하였습니다\"}"
                                    ),
                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "🚨 사용자 이메일 수정 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유저 조회 실패",
                                            value = "{\"error\" : \"404\", \"message\" : \"유저 조회에 실패하였습니다\"}"
                                    ),
                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PatchMapping("/email")
    ResponseEntity<ResponseDto<String>> updateUserEmail(@AuthenticationPrincipal User user,
                                                        @Valid @RequestBody EmailUpdateRequestDto request);

    @Operation(summary = "알람 활성화 시각 수정", description = "알람 활성화 시각을 수정합니다<br>" +
            "🔐 <strong>Jwt 필요</strong><br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 알람 활성화 시각 수정 성공"),
            @ApiResponse(responseCode = "400", description = "🚨 알람 활성화 시각 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유효성 검사 실패",
                                            value = "{\"error\" : \"400\", \"message\" : \"유효성 검사에 실패하였습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "🚨 알람 활성화 시각 수정 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유저 조회 실패",
                                            value = "{\"error\" : \"404\", \"message\" : \"유저 조회에 실패하였습니다\"}"
                                    ),
                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PatchMapping("/alarm")
    ResponseEntity<ResponseDto<String>> updateAlarmActiveTime(@AuthenticationPrincipal User user,
                                                              @Valid@RequestBody AlarmActiveTimeUpdateRequestDto request);
}
