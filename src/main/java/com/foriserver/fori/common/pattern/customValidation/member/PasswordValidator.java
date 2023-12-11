package com.foriserver.fori.common.pattern.customValidation.member;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

import com.foriserver.fori.common.pattern.custom.member.Password;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    // 비밀번호 정규식(영문 대/소문자, 숫자, 키패드 1~0까지의 특수문자만 가능하며 각각 한 글자 이상이 포함되어야 함)
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()])[a-z0-9!@#$%^&*()]+$");

    // 비밀번호 최소, 최대길이
    private int minLength;
    private int maxLength;

    // 비밀번호 요구사항을 만족하지 않을 경우 출력할 메시지
    private String message;
    private String patternMessage;
    private String lengthMessage;

    @Override
    public void initialize(Password constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
        this.maxLength = constraintAnnotation.maxLength();
        this.message = constraintAnnotation.message();
        this.patternMessage = constraintAnnotation.patternMessage();
        this.lengthMessage = constraintAnnotation.lengthMessage();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        int length = value.length();
        boolean patternMatch = PASSWORD_PATTERN.matcher(value).matches();

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
