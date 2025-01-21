package org.haruni.domain.oauth.common.utils;

import org.haruni.domain.oauth.common.entity.OAuth2UserInfo;
import org.haruni.domain.oauth.domain.google.entity.GoogleOAuth2UserInfo;
import org.haruni.domain.oauth.domain.kakao.entity.KakaoOAuth2UserInfo;
import org.haruni.domain.oauth.domain.naver.entity.NaverOAuth2UserInfo;
import org.haruni.global.exception.entity.RestApiException;
import org.haruni.global.exception.error.CustomErrorCode;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes, String accessToken){

        if (OAuth2Provider.GOOGLE.getRegistrationId().equals(registrationId)) return new GoogleOAuth2UserInfo(attributes, accessToken);
        if (OAuth2Provider.KAKAO.getRegistrationId().equals(registrationId)) return new KakaoOAuth2UserInfo(attributes, accessToken);
        if (OAuth2Provider.NAVER.getRegistrationId().equals(registrationId)) return new NaverOAuth2UserInfo(attributes, accessToken);

        throw new RestApiException(CustomErrorCode.OAUTH2_INVALID_PROVIDER);
    }
}
