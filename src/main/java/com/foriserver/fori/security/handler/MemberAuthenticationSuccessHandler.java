package com.foriserver.fori.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foriserver.fori.security.auth.service.FooriMemberDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MemberAuthenticationSuccessHandler implements AuthenticationSuccessHandler
{
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        FooriMemberDetailsService.FooriMemberDetails memberDetails = (FooriMemberDetailsService.FooriMemberDetails) authentication.getPrincipal();
        Map<String, Object> memberInfo = getMemberInfoByMemberDetails(memberDetails);

        String jsonResponse = new ObjectMapper().writeValueAsString(memberInfo);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);

        log.info("# Authenticated successfully!, 사용자 인증 성공!");
    }

    private static Map<String, Object> getMemberInfoByMemberDetails(FooriMemberDetailsService.FooriMemberDetails memberDetails) {
        Long memberId = memberDetails.getMemberId();
        String loginId = memberDetails.getLoginId();
        String email = memberDetails.getEmail();
        int birth = memberDetails.getBirth();
        String phoneNum = memberDetails.getPhoneNum();

        Map<String, Object> memberInfo = new HashMap<>();
        memberInfo.put("memberId", memberId);
        memberInfo.put("loginId", loginId);
        memberInfo.put("email", email);
        memberInfo.put("birth", birth);
        memberInfo.put("phoneNum", phoneNum);
        return memberInfo;
    }
}
