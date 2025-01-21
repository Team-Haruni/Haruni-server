package org.haruni.domain.oauth.common.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.haruni.global.exception.entity.RestApiException;
import org.haruni.global.exception.error.CustomErrorCode;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum OAuth2Provider {

    NORMAL("normal", "NULL"),
    GOOGLE("google","https://www.googleapis.com/oauth2/v3/userinfo"),
    KAKAO("kakao", "https://kapi.kakao.com/v2/user/me"),
    NAVER("naver", "https://openapi.naver.com/v1/nid/me");

    @JsonValue
    private final String registrationId;

    private final String providerEndpoint;

    @JsonCreator
    public static OAuth2Provider fromOAuth2Provider(String value){
        return Arrays.stream(values())
                .filter(p -> p.getRegistrationId().equals(value))
                .findAny()
                .orElse(null);
    }

    public static String getProviderEndPoint(String value){
        return Arrays.stream(values())
                .filter(p -> p.getRegistrationId().equals(value))
                .map(p -> p.getProviderEndpoint())
                .findFirst()
                .orElseThrow(() -> new RestApiException(CustomErrorCode.OAUTH2_INVALID_PROVIDER));
    }
}
