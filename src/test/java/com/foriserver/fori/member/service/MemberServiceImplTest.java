package com.foriserver.fori.member.service;

import com.foriserver.fori.member.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Slf4j
@Transactional
@SpringBootTest
class MemberServiceImplTest {

    @Autowired
    MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    Member hong;
    Member kim;

    @BeforeEach
    void setUp() {
        hong = new Member(1L, "홍길동", "hong123", "a123456!", 20010101, "hong123@test.com", "010-1234-5678");
        kim = new Member(2L, "김철수", "kim12", "a123456", 20110703, "kim123@test", "012-345-6789");
    }

    @Test
    @DisplayName("회원 생성")
    void createMember() {
        // 생성된 인스턴스의 정보로 회원 생성 후 savedMember에 저장
        Member savedmember = memberService.createMember(hong);

        // Verify
        assertThat(savedmember).isNotNull();
        assertThat(savedmember.getMemberId()).isEqualTo(hong.getMemberId());
        assertEquals(savedmember.getName(), hong.getName());
        assertEquals(savedmember.getLoginId(), hong.getLoginId());
        assertEquals(savedmember.getPassword(), hong.getPassword());
        assertEquals(savedmember.getBirth(), hong.getBirth());
        assertEquals(savedmember.getEmail(), hong.getEmail());
        assertEquals(savedmember.getPhoneNum(), hong.getPhoneNum());
    }

    @Test
    @DisplayName("회원 조회(이메일)")
    void findMemberByEmail() {
        // 생성된 인스턴스의 정보로 회원 생성 후 savedMember에 저장
        Member savedMember = memberService.createMember(hong);

        // 생성된 회원의 이메일로 조회
        Member findMember = memberService.findMemberByEmail(savedMember.getEmail());

        // Verify
        assertEquals(savedMember.getEmail(), findMember.getEmail());
        assertEquals(savedMember.getName(), findMember.getName());
        assertEquals(savedMember.getLoginId(), findMember.getLoginId());
        assertEquals(savedMember.getPassword(), findMember.getPassword());
        assertEquals(savedMember.getBirth(), findMember.getBirth());
        assertThat(savedMember.getPhoneNum()).isEqualTo(findMember.getPhoneNum());
        assertEquals(savedMember.getMemberId(), findMember.getMemberId());
    }

    @Test
    @DisplayName("회원 조회(로그인 아이디)")
    void findMemberByLoginId() {
        // 생성된 인스턴스의 정보로 회원 생성 후 savedMember에 저장
        Member savedMember = memberService.createMember(hong);

        // 생성된 회원의 로그인 아이디로 조회
        Member findMember = memberService.findMemberByLoginId(savedMember.getLoginId());

        // Verify
        assertEquals(savedMember.getLoginId(), findMember.getLoginId());
        assertEquals(savedMember.getName(), findMember.getName());
        assertEquals(savedMember.getEmail(), findMember.getEmail());
        assertEquals(savedMember.getPassword(), findMember.getPassword());
        assertEquals(savedMember.getBirth(), findMember.getBirth());
        assertEquals(savedMember.getPhoneNum(), findMember.getPhoneNum());
        assertEquals(savedMember.getMemberId(), findMember.getMemberId());
    }

    @Test
    @DisplayName("회원 조회(전화번호)")
    void findMemberByPhoneNum() {
        // 생성된 인스턴스의 정보로 회원 생성 후 savedMember에 저장
        Member savedMember = memberService.createMember(hong);

        // 생성된 회원의 전화번호로 조회
        Member findMember = memberService.findMemberByPhoneNum(savedMember.getPhoneNum());

        // Verify
        assertEquals(savedMember.getPhoneNum(), findMember.getPhoneNum());
        assertEquals(savedMember.getName(), findMember.getName());
        assertEquals(savedMember.getEmail(), findMember.getEmail());
        assertEquals(savedMember.getPassword(), findMember.getPassword());
        assertEquals(savedMember.getBirth(), findMember.getBirth());
        assertEquals(savedMember.getLoginId(), findMember.getLoginId());
        assertEquals(savedMember.getMemberId(), findMember.getMemberId());
    }

    @Test
    @DisplayName("회원 전체 조회")
    void findMembers() {
        Member savedHong = memberService.createMember(hong);
        Member savedKim = memberService.createMember(kim);

        // 조회
        List<Member> AllMembers = memberService.findMembers();

        // Verify
        assertThat(AllMembers.size()).isEqualTo(2);
        assertThat(AllMembers).containsExactly(savedHong, savedKim);
    }

    @Test
    @DisplayName("회원 수정")
    void updateMember() {
        // 생성된 인스턴스의 정보로 회원 생성 후 savedMember에 저장
        Member savedMember = memberService.createMember(hong);

        // 생성된 회원의 로그인 아이디로 조회
        Member findMember = memberService.findMemberByPhoneNum(savedMember.getPhoneNum());

        // 수정
        findMember.setName("김홍철");
        findMember.setEmail("kim321@test.com");

        // 변경된 정보를 저장
        Member updatedMember = memberService.updateMember(findMember, savedMember.getPassword());

        // Verify
        assertEquals(updatedMember.getName(), findMember.getName());
        assertEquals(updatedMember.getEmail(), findMember.getEmail());
    }

    @Test
    @DisplayName("회원 비밀번호 변경")
    void updateMemberPassword() {
        // 생성된 인스턴스의 정보로 회원 생성 후 savedMember에 저장
        Member savedMember = memberService.createMember(hong);

        // 생성된 회원의 로그인 아이디로 조회
        Member findMember = memberService.findMemberByPhoneNum(savedMember.getPhoneNum());

        // 변경할 비밀번호
        String newPassword = "a73210!";

        // 변경
        Member updatedMember = memberService.updateMemberPassword(findMember, newPassword);
        log.info("{}님의 비밀번호가 변경되었습니다.", updatedMember.getName());

        // Verify
        assertThat(passwordEncoder.matches(newPassword, updatedMember.getPassword())).isTrue();
    }

    @Test
    @DisplayName("회원 삭제")
    void deleteMember() {
        // 생성된 인스턴스의 정보로 회원 생성 후 savedMember에 저장
        Member savedMember = memberService.createMember(hong);

        // 생성된 회원의 로그인 아이디로 조회
        Member findMember = memberService.findMemberByPhoneNum(savedMember.getPhoneNum());

        // 삭제
        memberService.deleteMember(findMember, savedMember.getPassword());

        // Verify
        assertThat(memberService.findMembers().size()).isEqualTo(0);
    }
}