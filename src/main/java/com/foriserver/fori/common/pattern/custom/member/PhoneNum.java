package com.foriserver.fori.common.pattern.custom.member;

import com.foriserver.fori.common.pattern.customValidation.member.PhoneNumValidator;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = PhoneNumValidator.class)
@Documented
public @interface PhoneNum {

    String message() default "휴대폰 번호 요구사항을 만족하지 않습니다.";
    String patternMessage() default "휴대폰 번호 형식이 올바르지 않습니다.";
    String lengthMessage() default "휴대폰 번호는 {minLength}~{maxLength}자 사이여야 합니다.";
    Class[] groups() default {};
    Class[] payload() default {};

    int minLength() default 12; // 휴대폰 번호 최소길이
    int maxLength() default 13; // 휴대폰 번호 최대길이
}
