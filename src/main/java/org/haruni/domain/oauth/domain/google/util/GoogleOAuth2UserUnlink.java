package org.haruni.domain.oauth.domain.google.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.haruni.domain.oauth.common.utils.OAuth2UserUnlink;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleOAuth2UserUnlink implements OAuth2UserUnlink {

    private static final String URL = "https://oauth2.googleapis.com/revoke";
    private final RestTemplate restTemplate;

    @Override
    public void unlink(String accessToken){
        log.info("[GoogleOAuth2UserUnlink - unlink()] - In");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("token", accessToken);
        restTemplate.postForObject(URL, params, String.class);

        log.info("[GoogleOAuth2UserUnlink - unlink()] - In");
    }
}
