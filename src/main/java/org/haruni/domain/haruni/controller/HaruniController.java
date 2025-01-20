package org.haruni.domain.haruni.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.haruni.domain.common.dto.res.ResponseDto;
import org.haruni.domain.haruni.dto.res.MainPageResponseDto;
import org.haruni.domain.haruni.service.HaruniService;
import org.haruni.domain.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
