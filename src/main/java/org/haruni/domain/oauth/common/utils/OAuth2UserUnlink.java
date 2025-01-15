package org.haruni.domain.oauth.common.utils;


public interface OAuth2UserUnlink {

    // CONCEPT : 소셜 계정 연동 해제 인터페이스(google, kakao, naver 구현)
    void unlink(String accessToken);
}
