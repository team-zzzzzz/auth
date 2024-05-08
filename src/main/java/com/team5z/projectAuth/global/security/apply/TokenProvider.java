package com.team5z.projectAuth.global.security.apply;

import com.team5z.projectAuth.auth.domain.record.LoginRecord;
import io.jsonwebtoken.*;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
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
        } catch (MalformedJwtException me) {
            // invalid token
        } catch (ExpiredJwtException ee) {
            // token expired
            claims = ee.getClaims();
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

    /**
     * token 유효성 검사
     * @param token
     * @return TokenValidation
     */
    public TokenValidation validateToken(String token) {
        byte[] keyBytes = Decoders.BASE64.decode(env.getProperty("jwt.secret-key"));
        Key key = Keys.hmacShaKeyFor(keyBytes);
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return TokenValidation.SUCCESS_JWT;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            return TokenValidation.MALFORMED_JWT;
        } catch (ExpiredJwtException e) {
            return TokenValidation.EXPIRED_JWT;
        } catch (UnsupportedJwtException e) {
            return TokenValidation.UNSUPPORTED_JWT;
        } catch (IllegalArgumentException e) {
            return TokenValidation.ILLEGAL_ARGUMENT_JWT;
        }
    }

    /**
     * token 발급
     * @param authenticate
     * @return LoginRecord
     */
    public LoginRecord createToken(Authentication authenticate) {
        byte[] keyBytes = Decoders.BASE64.decode(env.getProperty("jwt.secret-key"));
        Key key = Keys.hmacShaKeyFor(keyBytes);

        // 권한들 가져오기
        String authorities = authenticate.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));


        LocalDateTime now = LocalDateTime.now();
        // LocalDateTime을 Instant로 변환
        LocalDateTime accessExpiration = now.plusMinutes(10L);  //토큰 유효시간은 10분
        Instant instant = accessExpiration.atZone(ZoneId.systemDefault()).toInstant();
        Date accessExpired = Date.from(instant);
        // Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(authenticate.getName())       // payload "sub": "name"
                .claim(AUTH_KEY, authorities)        // payload "auth": "ROLE_USER"
                .setExpiration(accessExpired)        // payload "exp": 1516239022 (예시)
                .signWith(key, SignatureAlgorithm.HS512)    // header "alg": "HS512"
                .compact();

        // Refresh Token 생성
        LocalDateTime refreshExpiration = now.plusMinutes(60L);  //토큰 유효시간은 60분
        instant = refreshExpiration.atZone(ZoneId.systemDefault()).toInstant();
        Date refreshExpired = Date.from(instant);
        String refreshToken = Jwts.builder()
                .setExpiration(refreshExpired)  // payload "exp": 1516239022 (예시)
                .signWith(key, SignatureAlgorithm.HS512)    // header "alg": "HS512"
                .compact();

        return LoginRecord.builder()
                .accessToken(accessToken)
                .accessTokenExpired(accessExpired.getTime())
                .refreshToken(refreshToken)
                .refreshTokenExpired(refreshExpired.getTime())
                .build();
    }
}
