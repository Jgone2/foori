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


    public Collection<? extends GrantedAuthority> createAuthorities(List<MemberRoles> roles) {
    }
}
