package com.foriserver.fori.common.exception.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {


    @Getter
    @AllArgsConstructor
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        public static FieldError of(org.springframework.validation.FieldError fieldError) {
            return new FieldError(fieldError.getField(), fieldError.getRejectedValue().toString(), fieldError.getDefaultMessage());
        }
    }
}
