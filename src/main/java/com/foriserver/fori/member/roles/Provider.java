package com.foriserver.fori.member.roles;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Provider {
    KAKAO("kakao회원입니다.");

    private String description;
}
