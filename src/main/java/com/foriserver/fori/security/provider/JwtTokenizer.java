package com.foriserver.fori.security.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenizer {

    // application.yml 파일에서 로드할 JWT 정보
    @Getter
    @Value("${jwt.key}")
    private String secretKey;

    @Getter
    @Value("${jwt.access-token-expiration-minutes}")
    private int accessTokenExpirationMinutes;

    @Getter
    @Value("${jwt.refresh-token-expiration-minutes}")
    private int refreshTokenExpirationMinutes;

    public String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // Access Token 생성
    public String generateAccessToken(Map<String, Object> claims,
                                      String subject,
                                      Date expiration,
                                      String base64EncodedSecretKey) {

        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        return Jwts.builder()
                .setClaims(claims)  // 토큰에 담을 정보
                .setSubject(subject)    // 토큰 제목
                .setIssuedAt(Calendar.getInstance().getTime())   // 토큰 발행 일자
                .setExpiration(expiration)  // 토큰 만료 시간
                .signWith(key, SignatureAlgorithm.HS256)   // 암호화 알고리즘, secretKey를 이용하여 암호화
                .compact(); // 토큰 생성 및 직렬화
    }

    // Refresh Token 생성
    public String generateRefreshToken(String subject, Date expiration, String base64EncodedSecretKey) {

        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 검증 후 유효한 토큰인지 확인
    public Jws<Claims> getClaims(String jws, String base64EncodedSecretKey) {

        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jws);

        return claimsJws;
    }

    //
    public void verifySignature(String jws, String base64EncodedSecretKey) {

        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jws);
    }

    // 토큰의 만료시간 지정 후 Date 객체로 반환
    public Date getTokenExpiration(int expirationMinutes) {

        Calendar calendar = Calendar.getInstance(); // getInstance() : 인스턴스 생성 후 현재 시스템의 날짜/시간 으로 초기화
        calendar.add(Calendar.MINUTE, expirationMinutes);  // 현재시간의 minute + expirationMinutes

        Date expiration = calendar.getTime();

        return expiration;
    }


    // base64로 인코딩된 secretKey를 복호화하여 key 객체로 변환
    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {

        byte[] decodedKeyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        Key key = Keys.hmacShaKeyFor(decodedKeyBytes);
        return key;
    }
}
