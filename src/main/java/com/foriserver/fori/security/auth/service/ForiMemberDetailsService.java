package com.foriserver.fori.security.auth.service;

import com.foriserver.fori.common.exception.ExceptionCode;
import com.foriserver.fori.member.entity.Member;
import com.foriserver.fori.member.repository.MemberRepository;
import com.foriserver.fori.security.utils.CustomAuthorityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ForiMemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final CustomAuthorityUtils authorityUtils;

    // 1. 권한 부여를 위해 loginId 로 유저 정보 (권한 포함) 불러옴
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
        Member findMember = optionalMember.orElseThrow(() -> new UsernameNotFoundException(loginId + ExceptionCode.MEMBER_NOT_FOUND));

        return new ForiMemberDetails(findMember);
    }

    public final class ForiMemberDetails extends Member implements UserDetails {

        ForiMemberDetails(Member member) {
            setMemberId(member.getMemberId());
            setLoginId(member.getLoginId());
            setPassword(member.getPassword());
            setBirth(member.getBirth());
            setPhoneNum(member.getPhoneNum());
            setEmail(member.getEmail());
//            setProfileImgPath(member.getProfileImgPath());
            setRoles(member.getRoles());  // 2. loadUserByUsername 메서드로 찾은 유저에게 권한 정보 전달
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            // loadUserByUsername 메서드로 찾은 유저에게 권한 정보 전달
            return authorityUtils.createAuthorities(this.getRoles());
        }

        @Override
        public String getUsername() {
            return getLoginId();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }


        // 유저의 권한 정보를 통해 권한이 활성화 되어있는지 확인
        // (권한이 활성화 되어있지 않으면 로그인 불가능)
        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
