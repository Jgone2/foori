package com.foriserver.fori.member.dto;

import com.foriserver.fori.common.pattern.custom.member.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class MemberDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Login {

        @LoginId
        private String loginId;
        @Password
        private String password;
    }

    @Getter
    @AllArgsConstructor
    public static class Post {

        @Name
        private String name;
        @LoginId
        private String loginId;
        @Password
        private String password;
        @Birth
        private int birth;
        @Email
        private String email;
        @PhoneNum
        private String phoneNum;
    }

    @Getter
    @AllArgsConstructor
    public static class Patch {

        @Name
        private String name;
        @Password
        private String password;
        @Email
        private String email;
        @PhoneNum
        private String phoneNum;
    }

    @Getter
    @AllArgsConstructor
    public static class CheckPassword {
        @Password
        private String password;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public abstract static class MemberResponse {
        private Long memberId;
        @Name
        private String name;
        @LoginId
        private String loginId;
        @Password
        private String password;
        @Birth
        private int birth;
        @Email
        private String email;
        @PhoneNum
        private String phoneNum;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberPostResponse extends MemberResponse {
        private LocalDateTime createdAt;

        public MemberPostResponse(Long memberId, String name, String loginId, String password, int birth, String email, String phoneNum, LocalDateTime createdAt) {
            super(memberId, name, loginId, password, birth, email, phoneNum);
            this.createdAt = createdAt;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberPatchResponse extends MemberResponse {
        private LocalDateTime modifiedAt;

        public MemberPatchResponse(Long memberId, String name, String loginId, String password, int birth, String email, String phoneNum, LocalDateTime modifiedAt) {
            super(memberId, name, loginId, password, birth, email, phoneNum);
            this.modifiedAt = modifiedAt;
        }
    }

}
