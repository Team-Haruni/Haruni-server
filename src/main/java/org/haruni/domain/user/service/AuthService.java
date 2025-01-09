package org.haruni.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.haruni.domain.haruni.entity.Haruni;
import org.haruni.domain.haruni.entity.MBTI;
import org.haruni.domain.haruni.repository.HaruniRepository;
import org.haruni.domain.user.dto.req.LoginRequestDto;
import org.haruni.domain.user.dto.req.SignUpRequestDto;
import org.haruni.domain.user.dto.res.TokenResponseDto;
import org.haruni.domain.user.entity.User;
import org.haruni.domain.user.repository.UserRepository;
import org.haruni.global.exception.entity.RestApiException;
import org.haruni.global.exception.error.CustomErrorCode;
import org.haruni.global.security.jwt.util.JwtTokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final HaruniRepository haruniRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public String signUp(SignUpRequestDto req){
        log.info("[AuthService - signUp()] : In");

        if(userRepository.existsByEmail((req.getEmail())))
            throw new RestApiException(CustomErrorCode.USER_EMAIL_DUPLICATED);

        log.info("[AuthService - signUp()] : Email duplication validate Finished");

        User user = User.builder()
                .req(req)
                .encodedPassword(passwordEncoder.encode(req.getPassword()))
                .build();

        userRepository.save(user);

        log.info("[AuthService - signUp()] : User saved");

        Haruni haruni = Haruni.builder()
                .name(req.getHaruniName())
                .mbti(MBTI.fromMBTI(req.getMbti()))
                .prompt(req.getPrompt())
                .build();

        haruni.matchUser(user);
        haruniRepository.save(haruni);

        log.info("[AuthService - signUp()] : Haruni saved");
        log.info("[AuthService - signUp()] : Out");

        return user.getEmail();
    }

    @Transactional
    public TokenResponseDto login(LoginRequestDto req){
        log.info("[AuthService - login()] : In");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        log.info("[AuthService - login()] : Authenticate user Succeed");

        return jwtTokenProvider.generateToken(authentication);
    }
}
