package org.haruni.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.haruni.domain.alarm.service.AlarmService;
import org.haruni.domain.haruni.repository.HaruniRepository;
import org.haruni.domain.oauth.common.utils.OAuth2Provider;
import org.haruni.domain.user.dto.req.AlarmActiveTimeUpdateRequestDto;
import org.haruni.domain.user.dto.req.EmailUpdateRequestDto;
import org.haruni.domain.user.dto.res.UserInfoResponseDto;
import org.haruni.domain.user.entity.User;
import org.haruni.domain.user.entity.UserDetailsImpl;
import org.haruni.domain.user.repository.UserRepository;
import org.haruni.global.exception.entity.RestApiException;
import org.haruni.global.exception.error.CustomErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final AlarmService alarmService;

    private final UserRepository userRepository;
    private final HaruniRepository haruniRepository;

    @Transactional(readOnly = true)
    public UserInfoResponseDto getUserInfo(UserDetailsImpl user){

        Boolean emailUpdateAvailable = user.getUser().getProviderId().equals(OAuth2Provider.NORMAL);

        log.info("[UserService - getUserInfo()] : 유저 정보 조회 성공");

        return UserInfoResponseDto.builder()
                .nickname(user.getUser().getNickname())
                .email(user.getUser().getEmail())
                .emailUpdateAvailable(emailUpdateAvailable)
                .build();
    }

    @Transactional
    public String updateEmail(UserDetailsImpl authUser, EmailUpdateRequestDto request){

        User user = userRepository.findByEmail(authUser.getUser().getEmail())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        if(userRepository.existsByEmail(request.getEmail()))
            throw new RestApiException(CustomErrorCode.USER_EMAIL_DUPLICATED);

        user.updateEmail(request.getEmail());

        log.info("[UserService - updateEmail()] : 유저 이메일 수정 성공");

        return request.getEmail();
    }

    @Transactional
    public String updateAlarmActiveTime(UserDetailsImpl authUser, AlarmActiveTimeUpdateRequestDto request){

         User user = userRepository.findByEmail(authUser.getUser().getEmail())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        user.updateAlarmActiveTime(request.getAlarmActiveTime());

        alarmService.updateAlarmSchedule(user.getFcmToken(), request.getAlarmActiveTime());

        log.info("[UserService - updateAlarmActiveTime()] : 유저 알람 활성화 시간 수정 성공");
        return request.getAlarmActiveTime();
    }

}