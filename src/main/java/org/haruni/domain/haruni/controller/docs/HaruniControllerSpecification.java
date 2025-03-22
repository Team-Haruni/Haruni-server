package org.haruni.domain.haruni.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.haruni.domain.chat.dto.req.ChatRequestDto;
import org.haruni.domain.chat.dto.res.ChatResponseDto;
import org.haruni.domain.common.dto.res.ResponseDto;
import org.haruni.domain.haruni.dto.req.PromptUpdateRequestDto;
import org.haruni.domain.haruni.dto.res.MainPageResponseDto;
import org.haruni.domain.user.entity.UserDetailsImpl;
import org.haruni.global.exception.error.ErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "HaruniController", description = "Haruni management Controller")
public interface HaruniControllerSpecification {

    @Operation(summary = "메인 페이지 조회", description = "메인 페이지에 로드할 정보를 조회합니다<br>" +
                                                       "🔐 <strong>Jwt 필요</strong><br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 메인 페이지 로드 성공"),
            @ApiResponse(responseCode = "404", description = "🚨 메인 페이지 로드 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유저 조회 실패",
                                            value = "{\"error\" : \"404\", \"message\" : \"유저 조회에 실패하였습니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "하루니 조회 실패",
                                            value = "{\"error\" : \"404\", \"message\" : \"하루니가 존재하지 않습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    ResponseEntity<ResponseDto<MainPageResponseDto>> getHaruni(@AuthenticationPrincipal UserDetailsImpl user);


    @Operation(summary = "프롬프트 수정", description = "하루니의 프롬프트를 수정합니다<br>" +
                                                     "🔐 <strong>Jwt 필요</strong><br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 하루니 프롬프트 수정 성공"),
            @ApiResponse(responseCode = "400", description = "🚨 월별 하루일기 조회 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유효성 검사 실패",
                                            value = "{\"error\" : \"400\", \"message\" : \"유효성 검사에 실패하였습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "🚨 하루니 프롬프트 수정 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유저 조회 실패",
                                            value = "{\"error\" : \"404\", \"message\" : \"유저 조회에 실패하였습니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "하루니 조회 실패",
                                            value = "{\"error\" : \"404\", \"message\" : \"하루니가 존재하지 않습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/prompts")
    ResponseEntity<ResponseDto<String>> updateUserPrompt(@AuthenticationPrincipal UserDetailsImpl user,
                                                         @Valid @RequestBody PromptUpdateRequestDto request);

    @Operation(summary = "메시지 전송", description = "하루니 모델 서버로 메시지를 전송합니다<br>" +
                                                   "🔐 <strong>Jwt 필요</strong><br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 메시지 전송 성공"),
            @ApiResponse(responseCode = "400", description = "🚨 메시지 전송 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유효성 검사 실패",
                                            value = "{\"error\" : \"400\", \"message\" : \"유효성 검사에 실패하였습니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "모델 서버로의 전송 실패",
                                            value = "{\"error\" : \"400\", \"message\" : \"하루니 서버로의 채팅 전송에 실패하였습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "🚨 메시지 전송 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유저 조회 실패",
                                            value = "{\"error\" : \"404\", \"message\" : \"유저 조회에 실패하였습니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "하루니 조회 실패",
                                            value = "{\"error\" : \"404\", \"message\" : \"하루니가 존재하지 않습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/chats")
    ResponseEntity<ResponseDto<ChatResponseDto>> sendChatToHaruni(@AuthenticationPrincipal UserDetailsImpl user,
                                                                  @Valid@RequestBody ChatRequestDto request);

    @Operation(summary = "메시지 조회", description = "특정 일의 메시지를 조회합니다<br>" +
                                                   "🔐 <strong>Jwt 필요</strong><br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 메시지 조회 성공"),
            @ApiResponse(responseCode = "400", description = "🚨 메시지 조회 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유효성 검사 실패",
                                            value = "{\"error\" : \"400\", \"message\" : \"유효성 검사에 실패하였습니다\"}"
                                    ),
                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "🚨 메시지 조회 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유저 조회 실패",
                                            value = "{\"error\" : \"404\", \"message\" : \"유저 조회에 실패하였습니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "채팅룸 조회 실패",
                                            value = "{\"error\" : \"404\", \"message\" : \"채팅룸이 존재하지 않습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/chats")
    ResponseEntity<ResponseDto<List<ChatResponseDto>>> getChats(@AuthenticationPrincipal UserDetailsImpl user,
                                                                @RequestParam
                                                                @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$",
                                                                        message = "날짜 형식은 YYYY-MM-DD 여야 합니다.")
                                                                String date);
}