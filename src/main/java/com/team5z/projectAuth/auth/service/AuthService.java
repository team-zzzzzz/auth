package com.team5z.projectAuth.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team5z.projectAuth.auth.domain.dto.LoginRequest;
import com.team5z.projectAuth.auth.domain.dto.MemberRequest;
import com.team5z.projectAuth.auth.domain.dto.TokenSaveDto;
import com.team5z.projectAuth.auth.domain.record.MemberResponse;
import com.team5z.projectAuth.auth.domain.record.LoginRecord;
import com.team5z.projectAuth.auth.domain.record.MessageRecord;
import com.team5z.projectAuth.auth.domain.entity.MemberEntity;
import com.team5z.projectAuth.auth.repository.MemberRepository;
import com.team5z.projectAuth.global.security.apply.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthService {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Transactional
    public MemberResponse join(MemberRequest memberRequest) {
        if (memberRequest.notEqualPasswordCheck()) {
            throw new IllegalArgumentException("password와 password_check가 동일하지 않습니다.");
        }

        // email 중복 확인
        Optional<MemberEntity> findMember = memberRepository.findByEmail(memberRequest.getEmail());
        if (findMember.isPresent()) {
            throw new IllegalArgumentException("이미 가입된 email 입니다.");
        }

        MemberEntity memberEntity = MemberEntity.from(memberRequest, passwordEncoder);

        MemberEntity saveMember = memberRepository.save(memberEntity);
        return MemberResponse.from(saveMember);
    }

    public MessageRecord findMemberByEmail(String email) {
        Optional<MemberEntity> findMember = memberRepository.findByEmail(email);
        if (findMember.isPresent()) {
            throw new IllegalArgumentException("이미 가입된 email 입니다.");
        }
        return MessageRecord.builder()
                .message("정상")
                .build();
    }

    public LoginRecord login(LoginRequest loginRequest) {
        // id, pw 기반으로 UsernamePasswordAuthenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();
        // security에 구현한 MyUserDetailsService 실행됨
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        LoginRecord loginRecord = tokenProvider.createToken(authenticate);
        TokenSaveDto tokenSaveDto = TokenSaveDto.from(authenticate);

        // token redis 저장
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();

        try {
            ops.set(loginRecord.refreshToken(), objectMapper.writeValueAsString(tokenSaveDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("tokenSaveDto convert string fail", e);
        }

        redisTemplate.expireAt(loginRecord.refreshToken(), Instant.ofEpochSecond(loginRecord.refreshTokenExpired()));

        return loginRecord;
    }

    public LoginRecord refresh(String refreshToken) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        String tokenSaveAsString = Optional.ofNullable(ops.get(refreshToken)).orElse("").toString();
        if (tokenSaveAsString.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 refresh token 입니다.");
        }

        TokenSaveDto tokenSaveDto = null;
        try {
            tokenSaveDto = objectMapper.readValue(tokenSaveAsString, TokenSaveDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("tokenSaveDto convert dto fail", e);
        }
        LoginRecord loginRecord = tokenProvider.createToken(tokenSaveDto);
        redisTemplate.delete(refreshToken);

        try {
            ops.set(loginRecord.refreshToken(), objectMapper.writeValueAsString(tokenSaveDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("tokenSaveDto convert string fail", e);
        }
        redisTemplate.expireAt(loginRecord.refreshToken(), Instant.ofEpochSecond(loginRecord.refreshTokenExpired()));
        return loginRecord;
    }
}
