package org.haruni.domain.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.haruni.domain.common.dto.res.ResponseDto;
import org.haruni.domain.user.dto.req.AlarmActiveTimeUpdateRequestDto;
import org.haruni.domain.user.dto.req.EmailUpdateRequestDto;
import org.haruni.domain.user.dto.res.UserInfoResponseDto;
import org.haruni.domain.user.entity.User;
import org.haruni.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "UserController", description = "User management Controller")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ResponseDto<UserInfoResponseDto>> getUserInfo(@AuthenticationPrincipal User user){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(userService.getUserInfo(user), "유저 정보 조회 완료"));
    }

    @PatchMapping("/email")
    public ResponseEntity<ResponseDto<String>> updateUserEmail(@AuthenticationPrincipal User user,
                                                               @Valid@RequestBody EmailUpdateRequestDto request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(userService.updateEmail(user,request), "유저 이메일 수정 완료"));
    }

    @PatchMapping("/alarm")
    public ResponseEntity<ResponseDto<String>> updateAlarmActiveTime(@AuthenticationPrincipal User user,
                                                                     @Valid@RequestBody AlarmActiveTimeUpdateRequestDto request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(userService.updateAlarmActiveTime(user, request), "유저 알람 활성화 시간 수정 완료"));
    }
}
