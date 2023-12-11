package com.foriserver.fori.common.pattern.customValidation.member;

import com.foriserver.fori.common.pattern.custom.member.Birth;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BirthValidator implements ConstraintValidator<Birth, String> {

    // 생년월일 정규표현식
    private static final String BIRTH_REGEX = "^[0-9]*$";

    // 생년월일 길이
    private int length;

    // 생년월일 최소크기
    private int minSize;

    // 생년월일 요구사항을 만족하지 않을 경우 출력할 메시지
    private String message;
    private String patternMessage;
    private String lengthMessage;
    private String minSizeMessage;

    @Override
    public void initialize(Birth constraintAnnotation) {
        this.length = constraintAnnotation.length();
        this.message = constraintAnnotation.message();
        this.patternMessage = constraintAnnotation.patternMessage();
        this.lengthMessage = constraintAnnotation.lengthMessage();
        this.minSize = constraintAnnotation.minSize();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;

        int length = value.length();
        int minSize = Integer.parseInt(value);
        boolean patternMatch = value.matches(BIRTH_REGEX);

        if (length != this.length || !patternMatch || minSize < this.minSize) {
            context.disableDefaultConstraintViolation();  // 기본 제약 조건을 비활성화.

            // 만약 길이 조건을 충족하지 않았다면 lengthMessage를 출력.
            if (length != this.length) {
                context.buildConstraintViolationWithTemplate(lengthMessage).addConstraintViolation();
            }

            // 만약 최소 크기 조건을 충족하지 않았다면 minSizeMessage를 출력.
            if (minSize < this.minSize) {
                context.buildConstraintViolationWithTemplate(minSizeMessage).addConstraintViolation();
            }

            // 만약 패턴 조건을 충족하지 않았다면 patternMessage를 출력.
            if (!patternMatch) {
                context.buildConstraintViolationWithTemplate(patternMessage).addConstraintViolation();
            }

            return false;
        }

        return true;
    }
}
