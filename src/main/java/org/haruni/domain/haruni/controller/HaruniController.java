package org.haruni.domain.haruni.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.haruni.domain.common.dto.res.ResponseDto;
import org.haruni.domain.haruni.dto.res.MainPageResponseDto;
import org.haruni.domain.haruni.service.HaruniService;
import org.haruni.domain.haruni.dto.req.PromptUpdateRequestDto;
import org.haruni.domain.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "HaruniController", description = "Haruni management Controller")
@RestController
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
}
