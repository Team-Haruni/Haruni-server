package org.haruni.global.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.haruni.global.exception.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
            throws ServletException, IOException{
        try{
            filterChain.doFilter(req, res);
        }catch (JwtException e){
            setErrorResponse(res, e);
        }
    }

    protected void setErrorResponse(HttpServletResponse res, JwtException e) throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();

        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorResponse<String> errorResponse = new ErrorResponse<>(
                HttpStatus.UNAUTHORIZED.value(),
                e.getMessage()
        );

        res.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
