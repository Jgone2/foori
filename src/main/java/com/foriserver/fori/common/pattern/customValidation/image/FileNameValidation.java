package com.foriserver.fori.common.pattern.customValidation.image;

import com.foriserver.fori.common.pattern.custom.image.FileName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileNameValidation implements ConstraintValidator<FileName, String> {

    // 파일명 정규표현식
    private static final String FILE_NAME_REGEX = "^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ_\\-]*$";

    // 파일명 최소, 최대길이
    private int minLength;
    private int maxLength;

    // 파일명 요구사항을 만족하지 않을 경우 출력할 메시지
    private String message;
    private String patternMessage;
    private String lengthMessage;


    @Override
    public void initialize(FileName constraintAnnotation) {
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
        boolean patternMatch = value.matches(FILE_NAME_REGEX);

        if (length < value.length() || length > this.maxLength || !patternMatch) {
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
