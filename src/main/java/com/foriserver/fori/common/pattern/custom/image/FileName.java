package com.foriserver.fori.common.pattern.custom.image;

import com.foriserver.fori.common.pattern.customValidation.image.FileNameValidation;
import jakarta.validation.Constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = FileNameValidation.class)
@Documented
public @interface FileName {

    String message() default "파일명 요구사항을 만족하지 않습니다.";
    String patternMessage() default "파일명은 한글, 영문 대/소문자, 숫자, 특수문자(_-)만 사용할 수 있습니다.";
    String lengthMessage() default "파일명은 {minLength}~{maxLength}자 사이여야 합니다.";
    Class[] groups() default {};
    Class[] payload() default {};

    int minLength() default 1; // 파일명 최소길이
    int maxLength() default 100; // 파일명 최대길이
}
