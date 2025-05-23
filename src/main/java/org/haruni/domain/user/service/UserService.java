package org.haruni.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.haruni.domain.alarm.service.AlarmService;
import org.haruni.domain.haruni.entity.Haruni;
import org.haruni.domain.haruni.repository.HaruniRepository;
import org.haruni.domain.oauth.common.utils.OAuth2Provider;
import org.haruni.domain.user.dto.req.AlarmActiveTimeUpdateRequestDto;
import org.haruni.domain.user.dto.res.UserInfoResponseDto;
import org.haruni.domain.user.entity.User;
import org.haruni.domain.user.entity.UserDetailsImpl;
import org.haruni.domain.user.repository.UserRepository;
import org.haruni.global.exception.entity.RestApiException;
import org.haruni.global.exception.error.CustomErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

    private final AlarmService alarmService;

    private final UserRepository userRepository;
    private final HaruniRepository haruniRepository;

    @Transactional(readOnly = true)
    public UserInfoResponseDto getUserInfo(UserDetailsImpl authUser){

        Boolean emailUpdateAvailable = authUser.getUser().getProviderId().equals(OAuth2Provider.NORMAL);

        Haruni haruni = haruniRepository.findByUserId(authUser.getUser().getId())
                        .orElseThrow(() -> new RestApiException(CustomErrorCode.HARUNI_NOT_FOUND));

        log.info("getUserInfo() : 유저 정보 조회 성공");

        return UserInfoResponseDto.builder()
                .haruniNickname(haruni.getName())
                .userNickname(authUser.getUser().getNickname())
                .email(authUser.getUser().getEmail())
                .emailUpdateAvailable(emailUpdateAvailable)
                .build();
    }

    @Transactional
    public String updateAlarmActiveTime(UserDetailsImpl authUser, AlarmActiveTimeUpdateRequestDto request){

         User user = userRepository.findByEmail(authUser.getUser().getEmail())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        user.updateAlarmActiveTime(request.getAlarmActiveTime());

        alarmService.updateAlarmSchedule(user.getFcmToken(), request.getAlarmActiveTime());

        log.info("updateAlarmActiveTime() : 유저 알람 활성화 시간 수정 성공");

        return request.getAlarmActiveTime();
    }

}