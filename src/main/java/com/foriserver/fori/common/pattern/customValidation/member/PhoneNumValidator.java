package com.foriserver.fori.common.pattern.customValidation.member;

import com.foriserver.fori.common.pattern.custom.member.PhoneNum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumValidator implements ConstraintValidator<PhoneNum, String> {

    // 휴대폰 번호 정규표현식
    private static final String PHONE_NUM_REGEX = "^(01[0-9]-\\d{3,4}-\\d{4})|(01[0-9]-\\d{4}-\\d{4})$";

    // 휴대폰 번호 최소, 최대길이
    private int minLength;
    private int maxLength;

    // 휴대폰 번호 요구사항을 만족하지 않을 경우 출력할 메시지
    private String message;
    private String patternMessage;
    private String lengthMessage;

    @Override
    public void initialize(PhoneNum constraintAnnotation) {
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
        boolean patternMatch = value.matches(PHONE_NUM_REGEX);

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

            return false;
        }

        return true;
    }
}
