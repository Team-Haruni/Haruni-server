package org.haruni.domain.oauth.exception.entity;

import org.springframework.security.core.AuthenticationException;

public class OAuth2AuthenticationException extends AuthenticationException{

    // CONCEPT : OAuth 관련 예외 클래스
    public OAuth2AuthenticationException(String message){
        super(message);
    }
}
