package org.haruni.domain.user.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.haruni.domain.alarm.service.AlarmService;
import org.haruni.domain.common.dto.res.ResponseDto;
import org.haruni.domain.user.controller.docs.UserControllerSpecification;
import org.haruni.domain.user.dto.req.AlarmActiveTimeUpdateRequestDto;
import org.haruni.domain.user.dto.req.EmailUpdateRequestDto;
import org.haruni.domain.user.dto.res.UserInfoResponseDto;
import org.haruni.domain.user.entity.UserDetailsImpl;
import org.haruni.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController implements UserControllerSpecification {

    private final UserService userService;
    private final AlarmService alarmService;

    @GetMapping
    public ResponseEntity<ResponseDto<UserInfoResponseDto>> getUserInfo(@AuthenticationPrincipal UserDetailsImpl user){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(userService.getUserInfo(user), "유저 정보 조회 완료"));
    }

    @PatchMapping("/me/email")
    public ResponseEntity<ResponseDto<String>> updateUserEmail(@AuthenticationPrincipal UserDetailsImpl user,
                                                               @Valid@RequestBody EmailUpdateRequestDto request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(userService.updateEmail(user,request), "유저 이메일 수정 완료"));
    }

    @PatchMapping("/me/alarm")
    public ResponseEntity<ResponseDto<String>> updateAlarmActiveTime(@AuthenticationPrincipal UserDetailsImpl user,
                                                                     @Valid@RequestBody AlarmActiveTimeUpdateRequestDto request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(userService.updateAlarmActiveTime(user, request), "유저 알람 활성화 시간 수정 완료"));
    }

    @GetMapping("/test")
    public void sendAlarmActive(){
        alarmService.alarmSendTest();
    }
}
