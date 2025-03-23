package org.haruni.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.haruni.domain.alarm.service.AlarmService;
import org.haruni.domain.haruni.entity.Haruni;
import org.haruni.domain.haruni.repository.HaruniRepository;
import org.haruni.domain.haruni.service.HaruniService;
import org.haruni.domain.user.dto.req.LoginRequestDto;
import org.haruni.domain.user.dto.req.SignUpRequestDto;
import org.haruni.domain.user.dto.res.TokenResponseDto;
import org.haruni.domain.user.entity.User;
import org.haruni.domain.user.repository.UserRepository;
import org.haruni.global.exception.entity.RestApiException;
import org.haruni.global.exception.error.CustomErrorCode;
import org.haruni.global.security.jwt.util.JwtTokenProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final HaruniService haruniService;
    private final AlarmService alarmService;

    private final UserRepository userRepository;
    private final HaruniRepository haruniRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    @Transactional
    public String signUp(SignUpRequestDto req){

        if(userRepository.existsByEmail((req.getEmail())))
            throw new RestApiException(CustomErrorCode.USER_EMAIL_DUPLICATED);

        if(userRepository.existsByFcmToken(req.getFcmToken()))
            throw new RestApiException(CustomErrorCode.USER_FCM_TOKEN_DUPLICATED);

        User user = User.builder()
                .req(req)
                .encodedPassword(passwordEncoder.encode(req.getPassword()))
                .build();

        userRepository.save(user);
        alarmService.updateAlarmSchedule(user.getFcmToken(), user.getAlarmActiveTime());

        log.info("[AuthService - signUp()] : 유저({}) 회원 가입 성공", user.getEmail());

        Haruni haruni = Haruni.builder()
                .name(req.getHaruniName())
                .prompt(req.getPrompt())
                .build();

        haruni.matchUser(user);
        haruniRepository.save(haruni);

        haruniService.createHaruniInstance(haruni.getId());

        return user.getEmail();
    }

    @Transactional
    public TokenResponseDto login(LoginRequestDto req){
        try{
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            log.info("[AuthService - login()] : 유저({}) 인증 성공", authentication.getName());

            return jwtTokenProvider.generateToken(authentication);
        }catch(BadCredentialsException | UsernameNotFoundException e) {
            throw new RestApiException(CustomErrorCode.USER_NOT_FOUND);
        }
    }
}
