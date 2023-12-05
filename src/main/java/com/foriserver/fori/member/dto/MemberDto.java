package com.foriserver.fori.member.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public class MemberDto {

    @Getter
    @AllArgsConstructor
    public static class Login {

        @Pattern(regexp = "^[A-Za-z0-9]+$", message = "아이디는 영문 대소문자와 숫자만 가능합니다.")
        @Length(min = 6, max = 15, message = "아이디는 6~15자 사이여야 합니다.")
        private String loginId;
        @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()])[a-z0-9!@#$%^&*()]+$",
                message = "비밀번호는 영문 소문자와 숫자, 키패드 1~0까지의 특수문자만 가능하며 각각 한 글자 이상이 포함되어야 합니다.")
        @Length(min = 8, max = 25, message = "비밀번호는 8~25자 사이여야 합니다.")
        private String password;
    }

    @Getter
    @AllArgsConstructor
    public static class Post {

        @Length(min = 1, max = 20, message = "이름은 1~20자 사이여야 합니다.")
        private String name;
        @Pattern(regexp = "^[A-Za-z0-9]+$", message = "아이디는 영문 대소문자와 숫자만 가능합니다.")
        @Length(min = 6, max = 15, message = "아이디는 6~15자 사이여야 합니다.")
        private String loginId;
        @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()])[a-z0-9!@#$%^&*()]+$",
                message = "비밀번호는 영문 소문자와 숫자, 키패드 1~0까지의 특수문자만 가능하며 각각 한 글자 이상이 포함되어야 합니다.")
        @Length(min = 8, max = 25, message = "비밀번호는 8~25자 사이여야 합니다.")
        private String password;
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "이메일 형식이 올바르지 않습니다.")
        private String email;
    }

    @Getter
    @AllArgsConstructor
    public static class Patch {

        @Length(min = 1, max = 20)
        private String name;
        @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()])[a-z0-9!@#$%^&*()]+$",
                message = "비밀번호는 영문 소문자와 숫자, 키패드 1~0까지의 특수문자만 가능하며 각각 한 글자 이상이 포함되어야 합니다.")
        @Length(min = 8, max = 25, message = "비밀번호는 8~25자 사이여야 합니다.")
        private String password;
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "이메일 형식이 올바르지 않습니다.")
        private String email;
    }

    @Getter
    @AllArgsConstructor
    public static class CheckPassword {
        @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()])[a-z0-9!@#$%^&*()]+$",
                message = "비밀번호는 영문 소문자와 숫자, 키패드 1~0까지의 특수문자만 가능하며 각각 한 글자 이상이 포함되어야 합니다.")
        @Length(min = 8, max = 25, message = "비밀번호는 8~25자 사이여야 합니다.")
        private String password;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public abstract static class MemberResponse {
        private Long memberId;
        @Length(min = 1, max = 20, message = "이름은 1~20자 사이여야 합니다.")
        private String name;
        @Pattern(regexp = "^[A-Za-z0-9]+$", message = "아이디는 영문 대소문자와 숫자만 가능합니다.")
        @Length(min = 6, max = 15, message = "아이디는 6~15자 사이여야 합니다.")
        private String loginId;
        @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()])[a-z0-9!@#$%^&*()]+$",
                message = "비밀번호는 영문 소문자와 숫자, 키패드 1~0까지의 특수문자만 가능하며 각각 한 글자 이상이 포함되어야 합니다.")
        @Length(min = 8, max = 25, message = "비밀번호는 8~25자 사이여야 합니다.")
        private String password;
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "이메일 형식이 올바르지 않습니다.")
        private String email;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberPostResponse extends MemberResponse {
        private LocalDateTime createdAt;

        public MemberPostResponse(Long memberId, String name, String loginId, String password, String email, LocalDateTime createdAt) {
            super(memberId, name, loginId, password, email);
            this.createdAt = createdAt;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberPatchResponse extends MemberResponse {
        private LocalDateTime modifiedAt;

        public MemberPatchResponse(Long memberId, String name, String loginId, String password, String email, LocalDateTime modifiedAt) {
            super(memberId, name, loginId, password, email);
            this.modifiedAt = modifiedAt;
        }
    }

}
