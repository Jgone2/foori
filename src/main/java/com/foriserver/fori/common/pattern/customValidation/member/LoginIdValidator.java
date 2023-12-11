package com.foriserver.fori.common.pattern.customValidation.member;

import com.foriserver.fori.common.pattern.custom.member.LoginId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class LoginIdValidator implements ConstraintValidator<LoginId, String> {

    // 아이디 정규식(영문 대소문자와 숫자만 가능)
    private static final Pattern LOGINID_PATTERN =
            Pattern.compile("^[A-Za-z0-9]+$");

    // 아이디 최소, 최대길이
    private int minLength;
    private int maxLength;

    // 아이디 요구사항을 만족하지 않을 경우 출력할 메시지
    private String message;
    private String patternMessage;
    private String lengthMessage;

    @Override
    public void initialize(LoginId constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
        this.maxLength = constraintAnnotation.maxLength();
        this.message = constraintAnnotation.message();
        this.patternMessage = constraintAnnotation.patternMessage();
        this.lengthMessage = constraintAnnotation.lengthMessage();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;

        int length = value.length();
        boolean patternMatch = LOGINID_PATTERN.matcher(value).matches();

        if (length < minLength || length > maxLength || !patternMatch) {
            context.disableDefaultConstraintViolation(); // 기본 제약 조건을 비활성화.

            // 만약 길이 조건을 충족하지 않았다면 lengthMessage를 출력.
            if (length < minLength) {
                context.buildConstraintViolationWithTemplate(lengthMessage).addConstraintViolation();
            }

            // 만약 길이 조건을 충족하지 않았다면 patternMessage를 출력.
            if (!patternMatch) {
                context.buildConstraintViolationWithTemplate(patternMessage).addConstraintViolation();
            }

            return false;
        }

        return true;
    }
}
