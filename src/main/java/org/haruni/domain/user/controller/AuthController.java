package org.haruni.domain.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.haruni.domain.common.dto.res.ResponseDto;
import org.haruni.domain.user.dto.req.SignUpRequestDto;
import org.haruni.domain.user.service.AuthService;
import org.haruni.domain.user.dto.req.LoginRequestDto;
import org.haruni.domain.user.dto.res.TokenResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AuthController", description = "Auth management Controller")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDto<String>> signUp(@Valid@RequestBody SignUpRequestDto req){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(authService.signUp(req), "회원가입 처리 완료"));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<TokenResponseDto>> login(@Valid@RequestBody LoginRequestDto req){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(authService.login(req), "로그인 처리 완료"));
    }
    
}