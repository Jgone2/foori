package com.foriserver.fori.security.oauth2.handler;

import com.foriserver.fori.member.service.MemberService;
import com.foriserver.fori.security.provider.JwtTokenizer;
import com.foriserver.fori.security.utils.CustomAuthorityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2MemberAuthenticationSuccessHandle extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;
    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = String.valueOf(oAuth2User.getAttributes().get("email"));
        String loginId = getLoginId(email);
        String password = randomPassword();
        String name = randomName();

    }

    private String getLoginId(String email) {
        StringBuffer sb = new StringBuffer();
        try {
            sb.append(email.substring(0, email.indexOf("@"));
        } catch (Exception e) {
            throw new RuntimeException("로그인 정보를 가져오는데 실패했습니다.");
        }
        return getLoginId();
    }
}
