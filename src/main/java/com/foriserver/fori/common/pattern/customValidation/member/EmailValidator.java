package com.foriserver.fori.common.pattern.customValidation.member;

import com.foriserver.fori.common.pattern.custom.member.Email;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<Email, String> {

    // 이메일 정규표현식(https://emailregex.com/)
    private static final String EMAIL_PATTERN =
            "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+" +
                    "(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
                    "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")" +
                    "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+" +
                    "[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.)" +
                    "{3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:" +
                    "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    // 이메일 최소, 최대길이
    private int minLength;
    private int maxLength;

    // 이메일 요구사항을 만족하지 않을 경우 출력할 메시지
    private String message;
    private String patternMessage;
    private String lengthMessage;
    @Override
    public void initialize(Email constraintAnnotation) {
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
        boolean patternMatch = value.matches(EMAIL_PATTERN);

        if (length < this.minLength || length > this.maxLength || !patternMatch) {
            context.disableDefaultConstraintViolation();  // 기본 제약 조건을 비활성화.

            // 만약 길이 조건을 충족하지 않았다면 lengthMessage를 출력.
            if (length < this.minLength || length > this.maxLength) {
                context.buildConstraintViolationWithTemplate(lengthMessage).addConstraintViolation();
            }

            // 만약 패턴 조건을 충족하지 않았다면 patternMessage를 출력.
            if (!patternMatch) {
                context.buildConstraintViolationWithTemplate(patternMessage).addConstraintViolation();
            }
        }

        return true;
    }
}
