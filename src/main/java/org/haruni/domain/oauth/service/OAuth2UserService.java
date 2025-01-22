package org.haruni.domain.oauth.service;

import lombok.extern.slf4j.Slf4j;
import org.haruni.domain.oauth.common.entity.OAuth2UserDetailsImpl;
import org.haruni.domain.oauth.common.entity.OAuth2UserInfo;
import org.haruni.domain.oauth.common.utils.OAuth2Provider;
import org.haruni.domain.oauth.common.utils.OAuth2UserInfoFactory;
import org.haruni.domain.oauth.dto.req.OAuthLoginRequestDto;
import org.haruni.domain.oauth.dto.res.OAuth2ResponseDto;
import org.haruni.domain.user.repository.UserRepository;
import org.haruni.global.exception.entity.RestApiException;
import org.haruni.global.exception.error.CustomErrorCode;
import org.haruni.global.security.jwt.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
public class OAuth2UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestTemplate oauthTemplate;

    public OAuth2UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, @Qualifier("oauthTemplate") RestTemplate oauthTemplate) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.oauthTemplate = oauthTemplate;
    }

    public OAuth2ResponseDto<?> oauth2LoginProcess(OAuthLoginRequestDto request) {
        log.info("[OAuth2UserService - oauth2LoginProcess()] : In");

        String endpoint = OAuth2Provider.getProviderEndPoint(request.getProviderId());

        log.info("[OAuth2UserService - oauth2LoginProcess()] : HttpRequest setting started");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(request.getAccessToken());

        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

        log.info("[OAuth2UserService - oauth2LoginProcess()] : HttpRequest sending process started");

        ResponseEntity<Map<String, Object>> response;
        try {
            response = oauthTemplate.exchange(
                    endpoint,
                    HttpMethod.GET,
                    httpEntity,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    }
            );
        } catch (HttpClientErrorException e) {
            log.error("[OAuth2UserService - oauth2LoginProcess()] : AccessToken is not available and rejected from resource server({})", request.getProviderId());
            throw new RestApiException(CustomErrorCode.OAUTH2_ACCESS_TOKEN_UNAVAILABLE);
        }

        log.info("[OAuth2UserService - oauth2LoginProcess()] : HttpResponse processing started");

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                request.getProviderId(),
                response.getBody(),
                request.getAccessToken()
        );

        if (userRepository.existsByEmail(oAuth2UserInfo.getEmail())) {
            log.info("[OAuth2UserService - oauth2LoginProcess()] : Generating access & refresh Token");

            OAuth2UserDetailsImpl oAuth2UserDetails = new OAuth2UserDetailsImpl(oAuth2UserInfo);
            Authentication authentication = new OAuth2AuthenticationToken(
                    oAuth2UserDetails,
                    oAuth2UserDetails.getAuthorities(),
                    request.getProviderId()
            );

            return OAuth2ResponseDto.login(jwtTokenProvider.generateToken(authentication));
        } else {
            log.info("[OAuth2UserService - oauth2LoginProcess()] : Generating redirect response");
            return OAuth2ResponseDto.signup(oAuth2UserInfo.getEmail());
        }
    }
}