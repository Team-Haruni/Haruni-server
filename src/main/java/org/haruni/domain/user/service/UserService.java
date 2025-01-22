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
    public UserInfoResponseDto getUserInfo(User user){
        log.info("[UserService - getUserInfo()] : In");

        Boolean emailUpdateAvailable = user.getProviderId().equals(OAuth2Provider.NORMAL);

        log.info("[UserService - getUserInfo()] : Out");

        return UserInfoResponseDto.builder()
                .nickname(user.getNickname())
                .email(user.getEmail())
                .emailUpdateAvailable(emailUpdateAvailable)
                .build();
    }

    @Transactional
    public String updateEmail(User authUser, EmailUpdateRequestDto request){
        log.info("[UserService - updateEmail()] : In");

        User user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        user.updateEmail(request.getEmail());

        log.info("[UserService - updateEmail()] : Out");

        return request.getEmail();
    }

    @Transactional
    public String updateAlarmActiveTime(User authUser, AlarmActiveTimeUpdateRequestDto request){
        log.info("[UserService - updateAlarmActiveTime()] : In");

        User user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        user.updateAlarmActiveTime(request.getAlarmActiveTime());

        log.info("[UserService - updateAlarmActiveTime()] : Update Scheduled Alarm in Redis");
        alarmService.updateAlarmSchedule(user.getFcmToken(), request.getAlarmActiveTime());

        log.info("[UserService - updateAlarmActiveTime()] : Out");
        return request.getAlarmActiveTime();
    }

}