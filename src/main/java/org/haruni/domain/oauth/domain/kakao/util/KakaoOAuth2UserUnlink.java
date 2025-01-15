package org.haruni.domain.oauth.domain.kakao.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.haruni.domain.oauth.common.utils.OAuth2UserUnlink;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoOAuth2UserUnlink implements OAuth2UserUnlink {

    private static final String URL = "https://kapi.kakao.com/v1/user/unlink";
    private final RestTemplate restTemplate;

    @Override
    public void unlink(String accessToken){
        log.info("[KakaoOAuth2UserUnlink - unlink()] - In");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);

        HttpEntity<Object> entity = new HttpEntity<>("", httpHeaders);
        restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);

        log.info("[KakaoOAuth2UserUnlink - unlink()] - Out");
    }
}
