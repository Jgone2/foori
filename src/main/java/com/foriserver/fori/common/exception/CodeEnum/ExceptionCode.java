package com.foriserver.fori.common.exception.CodeEnum;

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
    MEMBER_PASSWORD_NOT_CHANGE(409, 10202, "기존의 비밀번호와 같습니다. 비밀번호가 변경되지 않았습니다."),

    // Login
    BAD_CREDENTIALS(401, 10301, "아이디 또는 비밀번호가 일치하지 않습니다."),
    AUTHENTICATION_CREDENTIALS_NOT_FOUND(401, 10302, "인증 정보가 없습니다."),
    UNKNOWN_LOGIN_FAIL(401, 10302, "알 수 없는 로그인 오류입니다."),

    // Image
    FILE_NAME_GENERATE_FAIL(500, 10401, "파일 이름 생성에 실패하였습니다."),
    FILE_UPLOAD_FAIL(500, 10402, "파일 업로드에 실패하였습니다."),
    IMAGE_NOT_FOUND(404, 10403, "이미지 파일을 찾을 수 없습니다.");

    private final int status;
    private final int customCode;
    private final String message;

}
