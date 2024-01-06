package com.foriserver.fori.security.response;

import com.foriserver.fori.common.exception.response.ErrorResponse;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

public class ErrorResponder {

    public static void sendErrorResponse(HttpServletResponse response,
                                         HttpStatus status) throws IOException {

        Gson gson = new Gson();
        ErrorResponse errorResponse = ErrorResponse.of(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status.value());
        response.getWriter().write(gson.toJson(errorResponse, ErrorResponse.class));
    }

    public static void sendExpiredJwtExceptionError(HttpServletResponse response,
                                                    HttpStatus status) throws IOException {

        Gson gson = new Gson();
        ErrorResponse errorResponse = ErrorResponse.of(status, "JWT 유효기간이 만료되었습니다.");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status.value());
        response.getWriter().write(gson.toJson(errorResponse, ErrorResponse.class));
    }

    public static void sendRefreshTokenExceptionError(HttpServletResponse response,
                                                      HttpStatus status) throws IOException {

        Gson gson = new Gson();
        ErrorResponse errorResponse = ErrorResponse.of(status, "Refresh Token 이 유효하지 않습니다.");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status.value());
        response.getWriter().write(gson.toJson(errorResponse, ErrorResponse.class));
    }
}
