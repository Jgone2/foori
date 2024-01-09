package com.foriserver.fori.common.pattern.custom.image;

import com.foriserver.fori.common.pattern.customValidation.image.FilePathValidation;
import jakarta.validation.Constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = FilePathValidation.class)
@Documented
public @interface FilePath {

    String message() default "파일경로 요구사항을 만족하지 않습니다.";
    String lengthMessage() default "파일경로는 {minLength}~{maxLength}자 사이여야 합니다.";
    Class[] groups() default {};
    Class[] payload() default {};

    int minLength() default 1; // 파일경로 최소길이
    int maxLength() default 250; // 파일경로 최대길이
}
