package org.haruni.domain.user.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.haruni.domain.common.dto.res.ResponseDto;
import org.haruni.domain.user.controller.docs.UserControllerSpecification;
import org.haruni.domain.user.dto.req.AlarmActiveTimeUpdateRequestDto;
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

    @GetMapping
    public ResponseEntity<ResponseDto<UserInfoResponseDto>> getUserInfo(@AuthenticationPrincipal UserDetailsImpl authUser){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(userService.getUserInfo(authUser), "유저 정보 조회 완료"));
    }

    @PatchMapping("/me/alarm")
    public ResponseEntity<ResponseDto<String>> updateAlarmActiveTime(@AuthenticationPrincipal UserDetailsImpl authUser,
                                                                     @Valid@RequestBody AlarmActiveTimeUpdateRequestDto request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(userService.updateAlarmActiveTime(authUser, request), "유저 알람 활성화 시간 수정 완료"));
    }
}
