package com.foriserver.fori.member.service;

import com.foriserver.fori.member.entity.Member;

import java.util.List;

public interface MemberService {

    // 회원 생성
    Member createMember(Member member);

    // 회원 조회
    Member findMemberByEmail(String email);
    Member findMemberByLoginId(String loginId);

    // 회원 전체 조회
    List<Member> findMembers();

    // 회원 수정
    Member updateMember(Member member, String password);

    // 회원 비밀번호 변경
    Member updateMemberPassword(Member member, String password);

    // 회원 삭제
    void deleteMember(Member member, String password);
}
