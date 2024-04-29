package com.foriserver.fori.member.service;

import com.foriserver.fori.common.exception.CodeEnum.ExceptionCode;
import com.foriserver.fori.common.s3.service.S3Service;
import com.foriserver.fori.member.entity.Member;
import com.foriserver.fori.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final S3Service s3Service;

    // 회원 생성
    @Override
    public Member createMember(Member member) {

        // 중복 체크(로그인 아이디, 이메일)
        verifyExistsLoginId(member.getLoginId());
        verifyExistsEmail(member.getEmail());
        verifyExistsPhoneNum(member.getPhoneNum());

        // 비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encryptedPassword);

        // 랜덤 프로필 이미지
        String randomProfileImagePath = createRandomProfileImage(member);
        member.setProfileImgPath(randomProfileImagePath);

        Member savedMember = memberRepository.save(member);

        return savedMember;
    }

    // 회원 조회(이메일)
    @Override
    public Member findMemberByEmail(String email) {
        return findVerifyMemberByEmail(email);
    }

    // 회원 조회(로그인 아이디)
    @Override
    public Member findMemberByLoginId(String loginId) {
        return findVerifyMemberByLoginId(loginId);
    }

    // 회원 조회(전화번호)
    @Override
    public Member findMemberByPhoneNum(String phoneNum) {
        return findVerifyMemberByPhoneNum(phoneNum);
    }

    // 회원 전체 조회
    @Override
    public List<Member> findMembers() {
        List<Member> allMembers = memberRepository.findAll();
        return allMembers;
    }

    // 회원 수정
    @Override
    public Member updateMember(Member member, String password) {
        // 회원 조회
        Member findMember = findVerifyMemberByLoginId(member.getLoginId());

        checkPassword(findMember, password);

        // 변경할 정보 저장
        findMember.setName(member.getName());
        findMember.setEmail(member.getEmail());
        findMember.setPhoneNum(member.getPhoneNum());

        // 변경된 정보 저장
        Member updatedMember = memberRepository.save(findMember);

        log.info("{}님의 정보가 수정되었습니다. 수정된 정보: 이름={}, 이메일={}, 전화번호={}", findMember.getName(), updatedMember.getName(), updatedMember.getEmail(), updatedMember.getPhoneNum());
        return updatedMember;
    }

    // 회원 비밀번호 변경
    @Override
    public Member updateMemberPassword(Member member, String password) {
        Member findMember = findVerifyMemberByPhoneNum(member.getPhoneNum());
        // 변경할 비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(password);
        // 새로운 비밀번호와 이전 비밀번호가 같으면 변경하지 않음
        if (passwordEncoder.matches(findMember.getPassword(), encryptedPassword)) {
            throw new RuntimeException(ExceptionCode.MEMBER_PASSWORD_NOT_CHANGE.getMessage());
        }

        // 변경할 암호화된 비밀번호 저장
        findMember.setPassword(encryptedPassword);

        // 변경된 정보 저장
        Member updatedMember = memberRepository.save(findMember);

        return updatedMember;
    }

    // 회원 삭제
    @Override
    public void deleteMember(Member member, String password) {
        // 패스워드로 검증
        checkPassword(member, password);
        memberRepository.delete(member);
    }

    //  회원 랜덤 프로필 이미지 생성
    public String createRandomProfileImage(Member member) {
        String name = member.getName();
        String imagePath = generateProfileImageUrl(name, 5);
        return imagePath; // 프로필 이미지 경로 반환
    }

    // 랜덤 프로필 이미지를 위한 URL 생성
    private String generateProfileImageUrl(String name, int colorCount) {
        String[] colors = generateRandomColors(colorCount);
        StringBuilder urlBuilder = new StringBuilder("https://source.boringavatars.com/beam/100/");
        urlBuilder.append(name).append("?colors=");
        for (int i = 0; i < colors.length; i++) {
            if (i > 0) {
                urlBuilder.append(",");
            }
            urlBuilder.append(colors[i]);
        }
        System.out.println("urlBuilder = " + urlBuilder.toString());
        return urlBuilder.toString();
    }

    // 랜덤색상 5개 생성
    private String[] generateRandomColors(int count) {
        String[] colors = new String[count];
        for (int i = 0; i < count; i++) {
            Color color = generateRandomColor();
            colors[i] = String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
        }
        return colors;
    }

    private Color generateRandomColor() {
        Random random = new Random();
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        return new Color(red, green, blue);
    }

    // Verify
    private void verifyExistsLoginId(String loginId) {
        memberRepository.findByLoginId(loginId)
                .ifPresent(member -> {
            throw new RuntimeException(ExceptionCode.MEMBER_LOGIN_ID_EXISTS.getMessage());
        });
    }

    private void verifyExistsEmail(String email) {
        memberRepository.findByEmail(email)
                .ifPresent(member -> {
            throw new RuntimeException(ExceptionCode.MEMBER_EMAIL_EXISTS.getMessage());
        });
    }

    private void verifyExistsPhoneNum(String phoneNum) {
        memberRepository.findByPhoneNum(phoneNum)
                .ifPresent(member -> {
            throw new RuntimeException(ExceptionCode.MEMBER_PHONE_NUM_EXISTS.getMessage());
        });
    }

    private Member findVerifyMemberByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> {
            throw new RuntimeException(ExceptionCode.MEMBER_NOT_FOUND.getMessage());
        });
    }

    private Member findVerifyMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> {
            throw new RuntimeException(ExceptionCode.MEMBER_NOT_FOUND.getMessage());
        });
    }

    private Member findVerifyMemberByPhoneNum(String phoneNum) {
        return memberRepository.findByPhoneNum(phoneNum)
                .orElseThrow(() -> {
            throw new RuntimeException(ExceptionCode.MEMBER_NOT_FOUND.getMessage());
        });
    }

    private Boolean checkPassword(Member member, String password) {
        // 회원의 비밀번호 추출
        String memberPassword = member.getPassword();
        // 입력받은 비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(password);
        // 비밀번호가 일치하지 않으면 예외 발생
        if(!passwordEncoder.matches(memberPassword, encryptedPassword)) {
            throw new RuntimeException(ExceptionCode.MEMBER_PASSWORD_MISMATCH.getMessage());
        }
        // 비밀번호가 일치하면 true 반환
        return true;
    }
}
