package org.haruni.domain.oauth.dto.res;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.haruni.domain.user.dto.res.TokenResponseDto;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class OAuth2ResponseDto<T>{

    private final HttpStatus statusCode;
    private final T data;
    private final Boolean needSignup;

    public static OAuth2ResponseDto<TokenResponseDto> login(TokenResponseDto response){
        return new OAuth2ResponseDto<>(HttpStatus.OK, response, false);
    }

    public static OAuth2ResponseDto<String> signup(String email){
        return new OAuth2ResponseDto<>(HttpStatus.ACCEPTED, email, true);
    }

}