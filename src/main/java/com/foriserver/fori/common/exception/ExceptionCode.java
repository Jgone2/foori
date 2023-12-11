package com.foriserver.fori.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    // Member
    MEMBER_NOT_FOUND(404, 10001, "사용자를 찾을 수 없습니다."),
    MEMBER_EXISTS(409, 10002, "이미 존재하는 사용자입니다."),
    MEMBER_LOGIN_ID_EXISTS(409, 10003, "이미 존재하는 아이디 입니다."),
    MEMBER_EMAIL_EXISTS(409, 10004, "이미 존재하는 Email 입니다."),
    MEMBER_PHONE_NUM_EXISTS(409, 10005, "이미 존재하는 휴대폰 번호 입니다."),

    // Verify
    MEMBER_PASSWORD_MISMATCH(409, 10201, "비밀번호가 일치하지 않습니다."),
    MEMBER_PASSWORD_NOT_CHANGE(409, 10202, "기존의 비밀번호와 같습니다. 비밀번호가 변경되지 않았습니다.");

    private final int status;
    private final int customCode;
    private final String message;

}
