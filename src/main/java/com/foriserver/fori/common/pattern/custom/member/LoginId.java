package com.foriserver.fori.common.pattern.custom.member;

import com.foriserver.fori.common.pattern.customValidation.member.LoginIdValidator;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = LoginIdValidator.class)
@Documented
public @interface LoginId {

        String message() default "아이디 요구사항을 만족하지 않습니다.";
        String patternMessage() default "아이디는 영문 대소문자와 숫자만 가능합니다.";
        String lengthMessage() default "아이디는 {minLength}~{maxLength}자 사이여야 합니다.";
        Class[] groups() default {};
        Class[] payload() default {};

        int minLength() default 6; // 아이디 최소길이
        int maxLength() default 15; // 아이디 최대길이
}
