package org.haruni.domain.oauth.common.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.haruni.global.exception.entity.RestApiException;
import org.haruni.global.exception.error.CustomErrorCode;

import java.util.Arrays;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum OAuth2Provider {

    NORMAL("NORMAL", "NULL"),
    GOOGLE("GOOGLE","https://www.googleapis.com/oauth2/v3/userinfo"),
    KAKAO("KAKAO", "https://kapi.kakao.com/v2/user/me"),
    NAVER("NAVER", "https://openapi.naver.com/v1/nid/me");

    @JsonValue
    private final String registrationId;

    private final String providerEndpoint;

    @JsonCreator
    public static OAuth2Provider fromOAuth2Provider(String value){
        log.info("value = {}", value);
        return Arrays.stream(values())
                .filter(p -> p.getRegistrationId().equals(value))
                .findAny()
                .orElse(null);
    }

    public static String getProviderEndPoint(String value){
        return Arrays.stream(values())
                .filter(p -> p.getRegistrationId().equals(value))
                .map(OAuth2Provider::getProviderEndpoint)
                .findFirst()
                .orElseThrow(() -> new RestApiException(CustomErrorCode.OAUTH2_INVALID_PROVIDER));
    }
}
