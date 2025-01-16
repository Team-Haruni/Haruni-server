package org.haruni.domain.oauth.repository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.haruni.domain.oauth.common.utils.CookieUtils;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    // CONCEPT : OAuth 인증 과정에서 사용되는 정보를 쿠키로 관리할 때, 사용되는 메소드 유틸

    // CONCEPT : 쿠키의 Key 값 선언
    public static final String OAUTH2_AUTHORIZATION_REQUEST = "oauth2_authorization_request";
    public static final String REDIRECT_URI_PARAM = "redirect_uri";
    public static final String MODE_PARAM = "mode";
    private static final int COOKIE_EXPIRED_TIME = 100;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request){

        /**
         * 1) 요청에서 OAUTH2_AUTHORIZATION_REQUEST 의 Value 를 가져옴.
         * 2) OAuth2AuthorizationRequest 타입으로 역직렬화하여 반환.
         */

        return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST)
                .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authReq, HttpServletRequest request, HttpServletResponse response){

        /**
         * 1) OAuth2AuthorizationRequestRedirectFilter 에서 생성한 OAuth2AuthorizationRequest 를 직렬화하여 쿠키에 저장
         * 2) 프론트에서 설정한 REDIRECT_URI_PARAM, MODE_PARAM 을 쿠키에 저장
         */

        if(authReq == null){
            CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST);
            CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM);
            CookieUtils.deleteCookie(request, response, MODE_PARAM);

            return;
        }

        CookieUtils.setCookie(response, OAUTH2_AUTHORIZATION_REQUEST, CookieUtils.serialize(authReq), COOKIE_EXPIRED_TIME);

        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM);
        if(StringUtils.hasText(redirectUriAfterLogin))
            CookieUtils.setCookie(response, REDIRECT_URI_PARAM, redirectUriAfterLogin, COOKIE_EXPIRED_TIME);

        String mode = request.getParameter(MODE_PARAM);
        if(StringUtils.hasText(mode))
            CookieUtils.setCookie(response, MODE_PARAM, mode, COOKIE_EXPIRED_TIME);

    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse res){
        return this.loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response){
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM);
        CookieUtils.deleteCookie(request, response, MODE_PARAM);
    }
}