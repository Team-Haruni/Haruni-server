package org.haruni.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.haruni.domain.alarm.service.AlarmService;
import org.haruni.domain.haruni.entity.Haruni;
import org.haruni.domain.haruni.repository.HaruniRepository;
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

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AlarmService alarmService;

    private final UserRepository userRepository;
    private final HaruniRepository haruniRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    @Transactional
    public String signUp(SignUpRequestDto request){

        if(userRepository.existsByEmail((request.getEmail())))
            throw new RestApiException(CustomErrorCode.USER_EMAIL_DUPLICATED);

        if(userRepository.existsByFcmToken(request.getFcmToken()))
            throw new RestApiException(CustomErrorCode.USER_FCM_TOKEN_DUPLICATED);

        User user = User.builder()
                .request(request)
                .encodedPassword(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
        alarmService.updateAlarmSchedule(user.getFcmToken(), user.getAlarmActiveTime());

        Haruni haruni = Haruni.builder()
                .userId(user.getId())
                .name(request.getHaruniName())
                .personality(request.getHaruniPersonality())
                .build();

        haruniRepository.save(haruni);

        log.info("signUp() - 회원가입 완료 User/Haruni's PK = {}, {}", user.getId(), haruni.getId());

        return user.getEmail();
    }

    @Transactional
    public TokenResponseDto login(LoginRequestDto request){
        try{
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            log.info("login() : 유저({}) 인증 성공", authentication.getName());

            return jwtTokenProvider.generateToken(authentication);
        }catch(BadCredentialsException | UsernameNotFoundException e) {
            throw new RestApiException(CustomErrorCode.USER_NOT_FOUND);
        }
    }
}
