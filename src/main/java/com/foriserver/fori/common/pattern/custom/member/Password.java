package com.foriserver.fori.common.pattern.custom.member;

import com.foriserver.fori.common.pattern.customValidation.member.PasswordValidator;
import javax.validation.Constraint;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.*;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface Password{

    String message() default "비밀번호 요구사항을 만족하지 않습니다.";
    String patternMessage() default "비밀번호는 영문 대/소문자, 숫자, 키패드 1~0까지의 특수문자만 가능하며 각각 한 글자 이상이 포함되어야 합니다.";
    String lengthMessage() default "비밀번호는 {minLength}~{maxLength}자 사이여야 합니다.";
    Class[] groups() default {};
    Class[] payload() default {};


    int minLength() default 8; // 비밀번호 최소길이
    int maxLength() default 25; // 비밀번호 최대길이
}
