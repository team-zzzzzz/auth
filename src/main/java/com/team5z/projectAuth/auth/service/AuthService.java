package com.team5z.projectAuth.auth.service;

import com.team5z.projectAuth.auth.controller.dto.LoginRequest;
import com.team5z.projectAuth.auth.controller.dto.MemberRequest;
import com.team5z.projectAuth.auth.controller.dto.MemberResponse;
import com.team5z.projectAuth.auth.controller.record.LoginRecord;
import com.team5z.projectAuth.auth.controller.record.MessageRecord;
import com.team5z.projectAuth.auth.domain.entity.MemberEntity;
import com.team5z.projectAuth.auth.repository.MemberRepository;
import com.team5z.projectAuth.global.api.Response;
import com.team5z.projectAuth.global.security.apply.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final BCryptPasswordEncoder passwordEncoder;
    @Transactional
    public MemberResponse join(MemberRequest memberRequest) {
        if (memberRequest.notEqualPasswordCheck()) {
            throw new IllegalArgumentException("password와 password_check가 동일하지 않습니다.");
        }

        // todo email 중복 확인
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
        // id, pw 기반으로 UsernamePasswordAuthenticationToken 객체 생
        UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();
        // security에 구현한 AuthService가 실행됨
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        LoginRecord response = tokenProvider.createToken(authenticate);
        // todo token redis 저장

        return response;
    }
}
