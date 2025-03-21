package org.haruni.domain.haruni.controller.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.haruni.domain.chat.dto.req.ChatRequestDto;
import org.haruni.domain.chat.dto.res.ChatResponseDto;
import org.haruni.domain.common.dto.res.ResponseDto;
import org.haruni.domain.haruni.controller.docs.HaruniControllerSpecification;
import org.haruni.domain.haruni.dto.req.PromptUpdateRequestDto;
import org.haruni.domain.haruni.dto.res.MainPageResponseDto;
import org.haruni.domain.haruni.service.HaruniService;
import org.haruni.domain.user.entity.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/haruni")
public class HaruniController implements HaruniControllerSpecification {

    private final HaruniService haruniService;

    @GetMapping
    public ResponseEntity<ResponseDto<MainPageResponseDto>> getHaruni(@AuthenticationPrincipal UserDetailsImpl user){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(haruniService.getHaruni(user), "메인 패이지 조회 완료"));
    }

    @PatchMapping("/prompt")
    public ResponseEntity<ResponseDto<String>> updateUserPrompt(@AuthenticationPrincipal UserDetailsImpl user,
                                                                @Valid@RequestBody PromptUpdateRequestDto request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(haruniService.updatePrompt(user, request), "유저 프롬프트 수정 완료"));
    }

    @PostMapping("/chat")
    public ResponseEntity<ResponseDto<ChatResponseDto>> sendChatToHaruni(@AuthenticationPrincipal UserDetailsImpl user,
                                                                         @Valid@RequestBody ChatRequestDto request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(haruniService.sendChatToHaruni(user, request), "채팅 송수신 처리 완료"));
    }

    @GetMapping("/chat")
    public ResponseEntity<ResponseDto<List<ChatResponseDto>>> getChats(@AuthenticationPrincipal UserDetailsImpl user,
                                                                       @RequestParam
                                                                       @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$",
                                                                               message = "날짜 형식은 YYYY-MM-DD 여야 합니다.")
                                                                       String date){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(haruniService.getChats(user, date), "채팅 내역 조회 완료"));
    }
}
