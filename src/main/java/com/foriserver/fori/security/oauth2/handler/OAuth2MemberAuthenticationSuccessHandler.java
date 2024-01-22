package com.foriserver.fori.security.oauth2.handler;

import com.foriserver.fori.common.exception.CodeEnum.ExceptionCode;
import com.foriserver.fori.member.entity.Member;
import com.foriserver.fori.member.repository.MemberRepository;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2MemberAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = String.valueOf(oAuth2User.getAttributes().get("email"));
        String loginId = getRandomLoginId(email);
        String password = getRandomPassword();
        String name = getRandomName();
        String provider = String.valueOf(oAuth2User.getAttributes().get("provider"));
        List<String>  authorities = authorityUtils.createRoles(loginId);

        if (memberRepository.findByEmail(email).isEmpty() && memberRepository.findByLoginId(loginId).isEmpty()) {
            saveMember(email, loginId, password, name, provider);
        }
        redirect(request, response, loginId, authorities);
    }

    private String delegateAccessToken(String loginId, List<String> authorities) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("loginId", loginId);
        claims.put("roles", authorities);

        String subject = loginId;
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        return accessToken;
    }

    private String delegateRefreshToken(String loginId) {
        String subject = loginId;
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);

        return refreshToken;
    }

    // Client에게 jwt 전달
    private void redirect(HttpServletRequest request,
                                 HttpServletResponse response,
                                 String loginId,
                                 List<String> authorities) {
        String accessToken = delegateAccessToken(loginId, authorities);
        String refreshToken = delegateRefreshToken(loginId);

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Refresh", refreshToken);

        String uri = createURI(accessToken, refreshToken).toString();

        redirect(request, response, loginId, authorities);
    }

    // Redirect할 URI 생성
    private URI createURI(String accessToken, String refreshToken) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("accessToken", accessToken);
        queryParams.add("refreshToken", refreshToken);

        return UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .scheme("https")
                .host("localhost")
                .host("foori.co.kr")
                .port(8080)
                .port(80)
                .port(443)
                .queryParams(queryParams)
                .build()
                .toUri();
    }

    private void saveMember(String email, String loginId, String password, String name, String provider) {
        Member member = new Member(email, loginId, password, name, provider);
        memberService.createMember(member);
    }

    private String getRandomName() {
        StringBuffer randomName = new StringBuffer(3);
        /**
         * 일단 홍길동으로 지정하자..
         */
        randomName.append("홍길동");
        return randomName.toString();
    }

    private String getRandomPassword() {
        final int MIN_LENGTH = 8;
        final int MAX_LENGTH = 25;

        String password = generateRandomPassword(MIN_LENGTH, MAX_LENGTH);
        return password;
    }

    private String generateRandomPassword(int minLength, int maxLength) {
        final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
        final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String DIGITS = "0123456789";
        final String SPECIAL_CHARACTERS = "!@#$%^&*()";
        final String ALL_CHARACTERS = LOWERCASE + UPPERCASE + DIGITS + SPECIAL_CHARACTERS;
        final Pattern PASSWORD_PATTERN =
                Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()])[a-z0-9!@#$%^&*()]+$");
        final Random RANDOM = new Random();

        int length = RANDOM.nextInt(maxLength - minLength + 1) + minLength;
        StringBuilder password;
        do {
            password = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                password.append(ALL_CHARACTERS.charAt(RANDOM.nextInt(ALL_CHARACTERS.length())));
            }
        } while (!PASSWORD_PATTERN.matcher(password.toString()).matches());
        return password.toString();
    }

    // 랜덤 로그인 아이디 생성
    private String getRandomLoginId(String email) {
        StringBuffer randomLoginId = new StringBuffer();

        try {
            randomLoginId.append(email.substring(0, email.indexOf("@")));
            // 생성할 로그인 아이디가 이미 존재할때 랜덤 숫자 생성
            if(memberRepository.findByLoginId(randomLoginId.toString()).isPresent()) {
                randomLoginId.append("_");
                /**
                 * 현재 0 - 1000까지의 랜덤 숫자를 생성하고 있음
                 *  추후 로직 수정이 필요. 아직 좋은 방식이 생각이 안남...
                 */
                randomLoginId.append((int)(Math.random() * 1000));
            }
        } catch (Exception e) {
            throw new RuntimeException(ExceptionCode.LOGIN_ID_MAKE_FAIL.getMessage());
        }

        return randomLoginId.toString();
    }
}
