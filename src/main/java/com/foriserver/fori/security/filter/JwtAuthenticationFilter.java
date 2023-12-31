package com.foriserver.fori.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foriserver.fori.member.dto.MemberDto;
import com.foriserver.fori.member.entity.Member;
import com.foriserver.fori.security.provider.JwtTokenizer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    // DI 받은 AuthenticationManager 로 로그인 인증 정보를 전달받아 인증 여부 판단
    private final AuthenticationManager authenticationManager;
    private final JwtTokenizer jwtTokenizer;

    // 인증 시도 로직
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        ObjectMapper objectMapper = new ObjectMapper(); // 전달받은 인증 정보 역직렬화 위한 ObjectMapper 인스턴스 생성
        MemberDto.Login loginDto = objectMapper.readValue(request.getInputStream(), MemberDto.Login.class); // 역직렬화

        // 인증 정보를 포함한 UsernamePasswordAuthenticationToken 토큰 생성 (데이터 -> 유저네임패스워드토큰에 할당)
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getLoginId(), loginDto.getPassword());

        return authenticationManager.authenticate(authenticationToken); // authenticationManager 에게 인증처리 위임
    }

    // 인증 성공할 경우
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws ServletException, IOException, ServletException {

        // 1. 인증 성공 시, Authentication 객체에서 회원 정보를 받아옴
        Member member = (Member) authResult.getPrincipal();

        // 2. 회원 정보를 통해 JWT 토큰(Access, refresh) 생성
        String accessToken = delegateAccessToken(member);
        String refreshToken = delegateRefreshToken(member);

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Refresh", refreshToken);

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }

    // Access Token 생성
    private String delegateAccessToken(Member member) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("loginId", member.getLoginId());
        claims.put("roles", member.getRoles());

        String subject = member.getLoginId();
        Date tokenExpiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, tokenExpiration, base64EncodedSecretKey);

        return accessToken;
    }

    // Refresh Token 생성
    private String delegateRefreshToken(Member member) {

        String subject = member.getLoginId();
        Date tokenExpiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        String refreshToken = jwtTokenizer.generateRefreshToken(subject, tokenExpiration, base64EncodedSecretKey);



        return refreshToken;
    }
}
