package com.foriserver.fori.common.pattern.custom.member;

import com.foriserver.fori.common.pattern.customValidation.member.NameValidator;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = NameValidator.class)
@Documented
public @interface Name {

    String message() default "이름 요구사항을 만족하지 않습니다.";
    String lengthMessage() default "이름은 {minLength}~{maxLength}자 사이여야 합니다.";
    Class[] groups() default {};
    Class[] payload() default {};

    int minLength() default 1; // 이름 최소길이
    int maxLength() default 20; // 이름 최대길이
}
