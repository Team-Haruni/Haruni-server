package org.haruni.domain.oauth.domain.naver.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.haruni.domain.oauth.common.utils.OAuth2UserUnlink;
import org.haruni.global.exception.entity.RestApiException;
import org.haruni.global.exception.error.CustomErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverOAuth2UserUnlink implements OAuth2UserUnlink {

    private static final String URL = "https://nid.naver.com/oauth2.0/token";
    private final RestTemplate restTemplate;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret")
    String clientSecret;

    @Override
    public void unlink(String accessToken){
        log.info("[NaverOAuth2UserUnlink - unlink()] - In");

        String url = URL +
                "?service_provider=NAVER" +
                "&grant_type=delete" +
                "&client_id=" +
                clientId +
                "&client_secret=" +
                clientSecret +
                "&access_token=" +
                accessToken;

        UnlinkResponse response = restTemplate.getForObject(url, UnlinkResponse.class);

        if(response != null && !"success".equalsIgnoreCase(response.getResult())){
            log.error("[NaverOAuth2UserUnlink - unlink()] - Failed");
            throw new RestApiException(CustomErrorCode.OAUTH_NAVER_UNLINK_FAILED);
        }

        log.info("[NaverOAuth2UserUnlink - unlink()] - Out");
    }

    @Getter
    @RequiredArgsConstructor
    public static class UnlinkResponse{

        @JsonProperty
        private final String accessToken;;

        private final String result;
    }
}
