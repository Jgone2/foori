package com.foriserver.fori.security.filter;

import com.foriserver.fori.security.provider.JwtTokenizer;
import com.foriserver.fori.security.utils.CustomAuthorityUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class JwtVerificationFilter  extends OncePerRequestFilter {

    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

       try {
           Map<String, Object> claims = verifyJws(request);
           setAuthenticationToContext(claims);
       } catch (SignatureException se) {
           request.setAttribute("exception", se);
       } catch (ExpiredJwtException ee) {
              request.setAttribute("exception", ee);
         } catch (Exception e) {
              request.setAttribute("exception", e);
       }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String authorization = request.getHeader("Authorization");

        return authorization == null || !authorization.startsWith("Bearer ");
    }



    private Map<String, Object> verifyJws(HttpServletRequest request) {

        String jws = request.getHeader("Authorization").replace("Bearer ", "");
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        Map<String, Object> claims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();

        return claims;
    }

    private void setAuthenticationToContext(Map<String, Object> claims) {

        String loginId = (String) claims.get("loginId");
        Collection authorities = authorityUtils.createAuthorities((List) claims.get("role"));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
