package com.foriserver.fori.common.exception.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.foriserver.fori.common.exception.CodeEnum.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class ErrorResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int customCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<FieldError> filedErrors;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ConstraintViolationError> violationErrors;

    public ErrorResponse(int status, int customCode, String message) {
        this.status = status;
        this.customCode = customCode;
        this.message = message;
    }

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ErrorResponse(List<FieldError> filedErrors, List<ConstraintViolationError> violationErrors) {
        this.filedErrors = filedErrors;
        this.violationErrors = violationErrors;
    }

    public static ErrorResponse of(BindingResult bindingResult) {
        return new ErrorResponse(FieldError.of(bindingResult), null);
    }

    public static ErrorResponse of(Set<ConstraintViolation<?>> violations) {
        return new ErrorResponse(null, ConstraintViolationError.of(violations));
    }

    // Custom code added
    public static ErrorResponse of(ExceptionCode exceptionCode) {
        return new ErrorResponse(exceptionCode.getStatus(), exceptionCode.getCustomCode(), exceptionCode.getMessage());
    }

    public static ErrorResponse of(HttpStatus httpStatus) {
        return new ErrorResponse(httpStatus.value(), httpStatus.getReasonPhrase());
    }

    public static ErrorResponse of(HttpStatus httpStatus, String message) {
        return new ErrorResponse(httpStatus.value(), message);
    }

    @Getter
    @AllArgsConstructor
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        public static List<FieldError> of(BindingResult bindingResult) {

            List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();

            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage())
                    ).collect(Collectors.toList());
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ConstraintViolationError {
        private String field;
        private String value;
        private String reason;

        public static List<ConstraintViolationError> of(Set<ConstraintViolation<?>> constraintViolations) {

            return constraintViolations.stream()
                    .map(violation -> new ConstraintViolationError(
                            violation.getPropertyPath().toString(),
                            violation.getInvalidValue().toString(),
                            violation.getMessage())
                    ).collect(Collectors.toList());
        }
    }
}
