package org.haruni.domain.oauth.common.utils;

import lombok.RequiredArgsConstructor;
import org.haruni.domain.oauth.common.exception.entity.OAuth2AuthenticationProcessingException;
import org.haruni.domain.oauth.domain.google.util.GoogleOAuth2UserUnlink;
import org.haruni.domain.oauth.domain.kakao.util.KakaoOAuth2UserUnlink;
import org.haruni.domain.oauth.domain.naver.util.NaverOAuth2UserUnlink;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2UserUnlinkManager {

    private final GoogleOAuth2UserUnlink googleOAuth2UserUnlink;
    private final KakaoOAuth2UserUnlink kakaoOAuth2UserUnlink;
    private final NaverOAuth2UserUnlink naverOAuth2UserUnlink;

    public void unlink(OAuth2Provider provider, String accessToken){
        if (OAuth2Provider.GOOGLE.equals(provider)) {
            googleOAuth2UserUnlink.unlink(accessToken);
        } else if (OAuth2Provider.NAVER.equals(provider)) {
            naverOAuth2UserUnlink.unlink(accessToken);
        } else if (OAuth2Provider.KAKAO.equals(provider)) {
            kakaoOAuth2UserUnlink.unlink(accessToken);
        } else {
            throw new OAuth2AuthenticationProcessingException("Invalid Provider with " + provider.getRegistrationId());
        }
    }
}
