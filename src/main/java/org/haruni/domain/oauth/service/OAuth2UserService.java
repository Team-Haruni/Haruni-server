package org.haruni.domain.oauth.service;

import lombok.extern.log4j.Log4j2;
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

@Log4j2
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

        String endpoint = OAuth2Provider.getProviderEndPoint(request.getProviderId());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(request.getAccessToken());

        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

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
            log.error("oauth2LoginProcess() : AccessToken is not available and rejected from resource server({})", request.getProviderId());
            throw new RestApiException(CustomErrorCode.OAUTH2_ACCESS_TOKEN_UNAVAILABLE);
        }

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                request.getProviderId(),
                response.getBody(),
                request.getAccessToken()
        );

        log.info("oauth2LoginProcess() : [{}] 사용자 정보 불러오기 완료 - {}", oAuth2UserInfo.getProvider(), oAuth2UserInfo.getEmail());

        if (userRepository.existsByEmail(oAuth2UserInfo.getEmail())) {

            OAuth2UserDetailsImpl oAuth2UserDetails = new OAuth2UserDetailsImpl(oAuth2UserInfo);

            Authentication authentication = new OAuth2AuthenticationToken(
                    oAuth2UserDetails,
                    oAuth2UserDetails.getAuthorities(),
                    request.getProviderId()
            );

            log.info("oauth2LoginProcess() : Security 인증 및 JWT 발급 성공");

            return OAuth2ResponseDto.login(jwtTokenProvider.generateToken(authentication));
        } else {
            log.info("oauth2LoginProcess() : 서비스 회원 아님. 회원가입 페이지로 리디랙션");
            return OAuth2ResponseDto.signup(oAuth2UserInfo.getEmail());
        }
    }
}