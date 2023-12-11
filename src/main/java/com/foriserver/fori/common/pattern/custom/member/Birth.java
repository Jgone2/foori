package com.foriserver.fori.common.pattern.custom.member;

import com.foriserver.fori.common.pattern.customValidation.member.BirthValidator;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = BirthValidator.class)
@Documented
public @interface Birth {

        String message() default "생년월일 요구사항을 만족하지 않습니다.";
        String patternMessage() default "생년월일은 숫자만 가능합니다.";
        String lengthMessage() default "생년월일은 {length}자여야 합니다.";
        String minSizeMessage() default "{minSize}이후 출생자만 가입 가능합니다.";
        Class[] groups() default {};
        Class[] payload() default {};

        int length() default 8; // 생년월일 길이
        int minSize() default 19000101; // 생년월일 최소크기
}
