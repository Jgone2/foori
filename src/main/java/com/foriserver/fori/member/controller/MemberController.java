package com.foriserver.fori.member.controller;

import com.foriserver.fori.member.dto.MemberDto;
import com.foriserver.fori.member.entity.Member;
import com.foriserver.fori.member.mapper.MemberMapper;
import com.foriserver.fori.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper mapper;

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity postMember(@Valid @RequestBody MemberDto.Post memberPostDto) {

        Member member = mapper.memberPostDtoToMember(memberPostDto);
        memberService.createMember(member);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // 회원조회(로그인아이디)
    @GetMapping("/{member-login-id}")
    public ResponseEntity getMember(@PathVariable("member-login-id") String loginId) {

        Member findMember = memberService.findMemberByLoginId(loginId);


        return ResponseEntity.ok(mapper.memberToMemberPostResponseDto(findMember));
    }

    // 회원조회(이메일)

    // 회원 전체조회가 언제필요할까?
    // 팔로우 팔로워 기능 추가할때(검색) -> 추후 구현

    // 회원정보 수정
    @PatchMapping("/{member-login-id}")
    public ResponseEntity patchMember(@PathVariable("member-login-id") String loginId,
                                      @Valid @RequestBody MemberDto.Patch memberPatchDto) {

        Member findMember = memberService.findMemberByLoginId(loginId);
        Member updateMember = memberService.updateMember(findMember, memberPatchDto.getPassword());

        return ResponseEntity.ok(mapper.memberToMemberPatchResponseDto(updateMember));
    }

    // 비밀번호 변경


    // 회원탈퇴
    @DeleteMapping("/{member-login-id}")
    public ResponseEntity<?> deleteMember(@PathVariable("member-login-id") String loginId,
                                          @Valid  @RequestBody MemberDto.CheckPassword checkPasswordDto) {

        Member findMember = memberService.findMemberByLoginId(loginId);
        memberService.deleteMember(findMember, checkPasswordDto.getPassword());

        return ResponseEntity.noContent().build();
    }
}
