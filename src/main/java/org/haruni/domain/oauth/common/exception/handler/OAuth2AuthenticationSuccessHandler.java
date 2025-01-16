package org.haruni.domain.oauth.common.exception.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.haruni.domain.oauth.common.entity.OAuth2UserDetailsImpl;
import org.haruni.domain.oauth.common.utils.CookieUtils;
import org.haruni.domain.oauth.common.utils.OAuth2UserUnlinkManager;
import org.haruni.domain.oauth.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import org.haruni.domain.oauth.service.CustomOAuth2UserService;
import org.haruni.domain.user.dto.res.TokenResponseDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

import static org.haruni.domain.oauth.repository.HttpCookieOAuth2AuthorizationRequestRepository.MODE_PARAM;
import static org.haruni.domain.oauth.repository.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    //CONCEPT : OAuth 인증 성공시 동작하는 핸들러

    private final CustomOAuth2UserService oAuth2UserService;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final OAuth2UserUnlinkManager oAuth2UserUnlinkManager;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException{

        String targetUrl = setTargetUrl(request, response, authentication);

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String setTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{

        Optional<String> redirectUrl = CookieUtils.getCookie(request, REDIRECT_URI_PARAM)
                .map(Cookie::getValue);

        String targetUrl = redirectUrl.orElse(getDefaultTargetUrl());

        String mode = CookieUtils.getCookie(request, MODE_PARAM)
                .map(Cookie::getValue)
                .orElse("");

        OAuth2UserDetailsImpl userDetails = getOAuth2UserDetails(authentication);

        if (userDetails == null)
            return UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("error", "Login Failed with Authentication Error")
                    .build()
                    .toUriString();

        if ("login".equalsIgnoreCase(mode)){
            if (!oAuth2UserService.checkUserPresent(userDetails.getUsername())){
                return UriComponentsBuilder.fromUriString(targetUrl)
                        .path("/signup")
                        .queryParam("provider", userDetails.getUserInfo().getProvider())
                        .build()
                        .toUriString();
            }else{
                TokenResponseDto tokenResponseDto = customOAuth2UserService.oAuth2Login(authentication);
                 return UriComponentsBuilder.fromUriString(targetUrl)
                         .path("/main")
                         .queryParam("accessToken", tokenResponseDto.getAccessToken())
                         .queryParam("refreshToken", tokenResponseDto.getRefreshToken())
                         .build()
                         .toUriString();
            }
        }else if ("unlink".equalsIgnoreCase(mode)){
            oAuth2UserUnlinkManager.unlink(userDetails.getUserInfo().getProvider(), userDetails.getUserInfo().getAccessToken());

            return UriComponentsBuilder.fromUriString(targetUrl)
                    .build()
                    .toUriString();
        }

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", "Login Failed with unexpected Error")
                .build()
                .toUriString();
    }

    private OAuth2UserDetailsImpl getOAuth2UserDetails(Authentication authentication){

        Object principal = authentication.getPrincipal();

        if(principal instanceof OAuth2UserDetailsImpl)
            return (OAuth2UserDetailsImpl) principal;

        return null;
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}