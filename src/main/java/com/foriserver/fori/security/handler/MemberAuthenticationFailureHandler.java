package com.foriserver.fori.security.handler;

import com.foriserver.fori.common.exception.CodeEnum.ExceptionCode;
import com.foriserver.fori.common.exception.response.ErrorResponse;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

@Slf4j
public class MemberAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        log.error("# Authentication failed: {}", exception.getMessage());

        // error 클래스 비지정하는 방식 필요
        String errorMessage = switch (exception.getClass().getSimpleName()) {

            case "BadCredentialsException", "InternalAuthenticationServiceException", "UsernameNotFoundException"
                    -> ExceptionCode.BAD_CREDENTIALS.getMessage();
            case "AuthenticationCredentialsNotFoundException"
                    -> ExceptionCode.AUTHENTICATION_CREDENTIALS_NOT_FOUND.getMessage();

            default -> ExceptionCode.UNKNOWN_LOGIN_FAIL.getMessage();

        };

        sendErrorResponse(response, errorMessage);
    }

    private void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {

        Gson gson = new Gson();
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.UNAUTHORIZED, errorMessage);    // 401
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(gson.toJson(errorResponse, ErrorResponse.class));
    }
}
