package com.team5z.projectAuth.global.config.apply;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TokenProvider {
    private final Environment env;
    private final String AUTH_KEY = "auth";

    /**
     * token을 통해 인증 정보를 반환 받는다.
     * @param token
     * @return Authentication
     */
    public Authentication getAuthentication(String token) {

        // key 생성
        byte[] keyBytes = Decoders.BASE64.decode(env.getProperty("jwt.secret-key"));
        Key key = Keys.hmacShaKeyFor(keyBytes);

        // parseClaims
        Claims claims = null;
        try {
            JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
            claims = jwtParser.parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            // token expired
            claims = e.getClaims();
        }

        // claims authentication 가져오기
        String claimsBody = Optional.ofNullable(claims.get(AUTH_KEY)).orElse("").toString();
        List<GrantedAuthority> authorities = Arrays.stream(claimsBody.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // user 정보 만들기
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }
}
