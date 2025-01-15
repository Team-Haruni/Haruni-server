package org.haruni.domain.oauth.common.exception.entity;

import org.springframework.security.core.AuthenticationException;

public class OAuth2AuthenticationProcessingException extends AuthenticationException{

    // CONCEPT : OAuth 관련 예외 클래스
    public OAuth2AuthenticationProcessingException(String message){
        super(message);
    }
}
