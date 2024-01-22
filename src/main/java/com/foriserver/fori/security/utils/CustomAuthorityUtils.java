package com.foriserver.fori.security.utils;

import com.foriserver.fori.member.roles.MemberRoles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class CustomAuthorityUtils {

    private final List<GrantedAuthority> ADMIN_ROLES = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_MEMBER");
    private final List<GrantedAuthority> STORE_ADMIN_ROLES = AuthorityUtils.createAuthorityList("ROLE_STORE_ADMIN", "ROLE_MEMBER");
    private final List<GrantedAuthority> MEMBER_ROLES = AuthorityUtils.createAuthorityList("ROLE_MEMBER");
    private final List<GrantedAuthority> SOCIAL_MEMBER_ROLES = AuthorityUtils.createAuthorityList("ROLE_SOCIAL_MEMBER");


    private final List<String> ADMIN_ROLES_STRING = List.of("ADMIN", "MEMBER");
    private final List<String> STORE_ADMIN_ROLES_STRING = List.of("STORE_ADMIN", "MEMBER");
    private final List<String> MEMBER_ROLES_STRING = List.of("MEMBER");
    private final List<String> SOCIAL_MEMBER_ROLES_STRING = List.of("SOCIAL_MEMBER");



    public Collection<? extends GrantedAuthority> createAuthorities(List<MemberRoles> roles) {

        if (roles.contains(MemberRoles.ADMIN)) {
            return ADMIN_ROLES;
        } else if (roles.contains(MemberRoles.STORE_ADMIN)) {
            return STORE_ADMIN_ROLES;
        } else if (roles.contains(MemberRoles.SOCIAL_MEMBER)) {
            return SOCIAL_MEMBER_ROLES;
        } else {
            return MEMBER_ROLES;
        }
    }

    // 추후 소셜로그인 시 권한 설정가능하도록 변경 예정
    public List<String> createRoles(String loginId) {
        return SOCIAL_MEMBER_ROLES_STRING;
    }
}
