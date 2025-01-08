package org.haruni.global.security.jwt.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.haruni.domain.user.entity.UserDetailsImpl;
import org.haruni.domain.user.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtTokenProvider {

    // TODO CustomJwtException 만들어서 적용하기

    @Value("${jwt.access-token.expired-time}")
    private Long accessTokenExpiredTime;

    @Value("${jwt.refresh-token.expired-time}")
    private Long refreshTokenExpiredTime;

    private final Key key;

    private final UserDetailsServiceImpl userDetailsService;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret, UserDetailsServiceImpl userDetailsService){
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.userDetailsService = userDetailsService;
    }

    public String getTokenFromRequest(HttpServletRequest req){
        return req
                .getHeader("Authorization")
                .substring(7);
    }

    public boolean validateAccessToken(String accessToken){
        if(accessToken == null)
            throw new JwtException("AccessToken is null");

        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
            return true;
        } catch (ExpiredJwtException e) {
            throw new JwtException("토큰이 만료되었습니다.");
        } catch (MalformedJwtException e) {
            throw new JwtException("토큰의 형식이 옳바르지 않습니다.");
        } catch (SignatureException | SecurityException e) {
            throw new JwtException("토큰의 서명이 옳바르지 않습니다.");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("토큰의 형식이 만료되었습니다. ");
        }
    }

    public Authentication getAuthentication(String accessToken){
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(getEmailFromToken(accessToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getEmailFromToken(String accessToken){
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getSubject();
    }
}
