package org.haruni.domain.oauth.common.exception.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.haruni.domain.oauth.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import org.haruni.domain.oauth.common.utils.CookieUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static org.haruni.domain.oauth.repository.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    // CONCEPT : OAuth 인증 실패시 동작하는 핸들러
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    /**
     *  1) 요청에서 redirect_uri 가져옴 (없으면 기본값 "/" 로 새팅)
     *  2) Query Parameter 로 에러 새팅
     *  3) 쿠키에 설정해둔 인증 값 초기화
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException{

        log.info("[Oauth2AuthenticationFailureHandler - onAuthenticationFailure()] - In");

        String targetUrl = CookieUtils.getCookie(request, REDIRECT_URI_PARAM)
                .map(Cookie::getValue)
                .orElse("/");

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", exception.getLocalizedMessage())
                .build().toUriString();

        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);

        log.info("[Oauth2AuthenticationFailureHandler - onAuthenticationFailure()] - Out");
    }

}
