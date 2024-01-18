package com.foriserver.fori.common.pattern.customValidation.image;

import com.foriserver.fori.common.pattern.custom.image.FilePath;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FilePathValidation implements ConstraintValidator<FilePath, String> {

    // 파일경로 최소, 최대길이
    private int minLength;
    private int maxLength;

    // 파일경로 요구사항을 만족하지 않을 경우 출력할 메시지
    private String message;
    private String lengthMessage;

    @Override
    public void initialize(FilePath constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
        this.maxLength = constraintAnnotation.maxLength();
        this.message = constraintAnnotation.message();
        this.lengthMessage = constraintAnnotation.lengthMessage();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null) return false;

        int length = value.length();

        if (length < this.minLength || length > this.maxLength) {
            context.disableDefaultConstraintViolation();  // 기본 제약 조건을 비활성화.
            context.buildConstraintViolationWithTemplate(lengthMessage).addConstraintViolation(); // 만약 길이 조건을 충족하지 않았다면 lengthMessage를 출력.
        }

        return true;
    }
}
