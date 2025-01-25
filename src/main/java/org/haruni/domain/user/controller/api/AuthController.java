package org.haruni.domain.user.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.haruni.domain.common.dto.res.ResponseDto;
import org.haruni.domain.oauth.dto.req.OAuthLoginRequestDto;
import org.haruni.domain.oauth.service.OAuth2UserService;
import org.haruni.domain.user.controller.docs.AuthControllerSpecification;
import org.haruni.domain.user.dto.req.LoginRequestDto;
import org.haruni.domain.user.dto.req.SignUpRequestDto;
import org.haruni.domain.user.dto.res.TokenResponseDto;
import org.haruni.domain.user.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController implements AuthControllerSpecification {

    private final AuthService authService;
    private final OAuth2UserService oAuth2UserService;

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDto<String>> signUp(@Valid@RequestBody SignUpRequestDto request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(authService.signUp(request), "회원가입 처리 완료"));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<TokenResponseDto>> login(@Valid@RequestBody LoginRequestDto request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(authService.login(request), "로그인 처리 완료"));
    }

    @PostMapping("/oauth/login")
    public ResponseEntity<?> oauthLogin(@Valid@RequestBody OAuthLoginRequestDto request){
        return ResponseEntity.status(HttpStatus.OK).body(oAuth2UserService.oauth2LoginProcess(request));
    }
    
}