package org.haruni.domain.haruni.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.haruni.domain.chat.dto.req.ChatRequestDto;
import org.haruni.domain.chat.dto.res.ChatResponseDto;
import org.haruni.domain.common.dto.res.ResponseDto;
import org.haruni.domain.haruni.dto.req.PromptUpdateRequestDto;
import org.haruni.domain.haruni.dto.res.MainPageResponseDto;
import org.haruni.domain.haruni.service.HaruniService;
import org.haruni.domain.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "HaruniController", description = "Haruni management Controller")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/haruni")
public class HaruniController {

    private final HaruniService haruniService;

    @GetMapping
    public ResponseEntity<ResponseDto<MainPageResponseDto>> getHaruni(@AuthenticationPrincipal User user){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(haruniService.getHaruni(user), "메인 패이지 조회 완료"));
    }

    @PatchMapping("/prompt")
    public ResponseEntity<ResponseDto<String>> updateUserPrompt(@AuthenticationPrincipal User user,
                                                                @Valid @RequestBody PromptUpdateRequestDto request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(haruniService.updatePrompt(user, request), "유저 프롬프트 수정 완료"));
    }

    @PostMapping("/chat")
    public ResponseEntity<ResponseDto<ChatResponseDto>> sendChatToHaruni(@AuthenticationPrincipal User user,
                                                                       @Valid@RequestBody ChatRequestDto request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(haruniService.sendChatToHaruni(user, request), "채팅 송수신 처리 완료"));
    }

    @GetMapping("/chat")
    public ResponseEntity<ResponseDto<List<ChatResponseDto>>> getChats(@AuthenticationPrincipal User user,
                                                                       @RequestParam
                                                                       @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$",
                                                                               message = "날짜 형식은 YYYY-MM-DD 여야 합니다.")
                                                                       String date){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(haruniService.getChats(user, date), "채팅 내역 조회 완료"));
    }
}
