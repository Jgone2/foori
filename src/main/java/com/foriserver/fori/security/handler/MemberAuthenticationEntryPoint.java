package com.foriserver.fori.security.handler;

import com.foriserver.fori.security.response.ErrorResponder;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class MemberAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        Exception exception = (Exception) request.getAttribute("exception");

        if (request.getAttribute("exception") instanceof ExpiredJwtException) {
            ErrorResponder.sendExpiredJwtExceptionError(response, HttpStatus.FORBIDDEN); // 403
        }
        ErrorResponder.sendErrorResponse(response, HttpStatus.UNAUTHORIZED); // 401

        logExceptionMessage(authException, exception);
    }

    // 유효하지 않은 JWT 또는 만료된 JWT 를 받게될 경우 error log
    private void logExceptionMessage(AuthenticationException authException, Exception exception) {

        String message = exception != null ? exception.getMessage() : authException.getMessage();
        log.warn("Unauthorized error happened: {}", message);
    }
}
