package com.foriserver.fori.member.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MEMBERS")
public class Member {
    /**
     * 회원 정보
     * 1. 회원 번호 - PK
     * 2. 이름
     * 3. 아이디
     * 4. 비밀번호
     * 5. 생년월일
     * 6. 이메일
     * 7. 휴대폰 번호
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    @Column(nullable = false, unique = false, updatable = true, length = 20)
    private String name;
    @Column(nullable = false, unique = true, updatable = false, length = 15)
    private String loginId;
    @Column(nullable = false, unique = false, updatable = true)
    private String password;
    @Column(nullable = false, unique = false, updatable = false, length = 8)
    private int birth;
    @Column(nullable = false, unique = true, updatable = true, length = 30)
    private String email;
    @Column(nullable = false, unique = true, updatable = true, length = 13)
    private String phoneNum;

    /**
     * 이미지(프로필 이미지)
     */
}
