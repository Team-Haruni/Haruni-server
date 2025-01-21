package org.haruni.domain.oauth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.haruni.domain.oauth.common.entity.OAuth2UserDetailsImpl;
import org.haruni.domain.oauth.common.entity.OAuth2UserInfo;
import org.haruni.domain.oauth.common.exception.entity.OAuth2AuthenticationProcessingException;
import org.haruni.domain.oauth.common.utils.OAuth2Provider;
import org.haruni.domain.oauth.common.utils.OAuth2UserInfoFactory;
import org.haruni.domain.oauth.dto.req.OAuthLoginRequestDto;
import org.haruni.domain.oauth.dto.res.OAuth2ResponseDto;
import org.haruni.domain.user.dto.res.TokenResponseDto;
import org.haruni.domain.user.repository.UserRepository;
import org.haruni.global.security.jwt.util.JwtTokenProvider;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestTemplate restTemplate;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        try{
            return convertToOAuth2Principal(userRequest, oAuth2User);
        }catch (AuthenticationException e){
            throw e;
        }catch (Exception e){
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private OAuth2User convertToOAuth2Principal(OAuth2UserRequest userRequest, OAuth2User oAuth2User){

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String accessToken = userRequest.getAccessToken().getTokenValue();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes(), accessToken);

        if(!StringUtils.hasText(oAuth2UserInfo.getEmail()))
            throw new OAuth2AuthenticationProcessingException("Email not found with OAuth2UserInfo");

        return new OAuth2UserDetailsImpl(oAuth2UserInfo);
    }

    public Boolean checkUserPresent(String email){
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public TokenResponseDto oAuth2Login(Authentication authentication){
        log.info("[AuthService - oAuth2Login()] : In");

        return jwtTokenProvider.generateToken(authentication);
    }

    public OAuth2ResponseDto<?> oauth2LoginProcess(OAuthLoginRequestDto request){
        log.info("[AuthService - oauth2LoginProcess()] : In");

        String endpoint = OAuth2Provider.getProviderEndPoint(request.getProviderId());

        log.info("[AuthService - oauth2LoginProcess()] : HttpRequest setting started");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(request.getAccessToken());

        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

        log.info("[AuthService - oauth2LoginProcess()] : HttpRequest sending process started");

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                endpoint,
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        log.info("[AuthService - oauth2LoginProcess()] : HttpResponse processing started");

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                request.getProviderId(),
                response.getBody(),
                request.getAccessToken()
        );

        if(userRepository.existsByEmail(oAuth2UserInfo.getEmail())){
            log.info("[AuthService - oauth2LoginProcess()] : Generating access & refresh Token");

            OAuth2UserDetailsImpl oAuth2UserDetails = new OAuth2UserDetailsImpl(oAuth2UserInfo);
            Authentication authentication = new OAuth2AuthenticationToken(
                    oAuth2UserDetails,
                    oAuth2UserDetails.getAuthorities(),
                    request.getProviderId()
            );

            return OAuth2ResponseDto.login(jwtTokenProvider.generateToken(authentication));
        }else{
            log.info("[AuthService - oauth2LoginProcess()] : Generating redirect response");
            return OAuth2ResponseDto.signup(oAuth2UserInfo.getEmail());
        }
    }
}