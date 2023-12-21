package com.foriserver.fori.member.roles;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRoles {

    // 관리자 권한
    ADMIN("ROLE_ADMIN"),

    // 가게 관리자 권한
    STORE_ADMIN("ROLE_STORE_ADMIN"),

    // 일반 회원 권한
    MEMBER("ROLE_MEMBER");

    private String memberRole;
}
