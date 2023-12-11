package com.foriserver.fori.common.pattern.custom.member;

import com.foriserver.fori.common.pattern.customValidation.member.EmailValidator;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@Documented
public @interface Email {

        String message() default "이메일 요구사항을 만족하지 않습니다.";
        String patternMessage() default "이메일 형식이 올바르지 않습니다.";
        String lengthMessage() default "이메일은 {minLength}~{maxLength}자 사이여야 합니다.";
        Class[] groups() default {};
        Class[] payload() default {};

        int minLength() default 6; // 이메일 최소길이
        int maxLength() default 30; // 이메일 최대길이
}
