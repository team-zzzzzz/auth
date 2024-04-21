package com.team5z.projectAuth.auth.service;

import com.team5z.projectAuth.auth.controller.dto.MemberRequest;
import com.team5z.projectAuth.auth.controller.dto.MemberResponse;
import com.team5z.projectAuth.auth.domain.entity.MemberEntity;
import com.team5z.projectAuth.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    public ResponseEntity<MemberResponse> join(MemberRequest memberRequest) {
        if (memberRequest.notEqualPasswordCheck()) {
            throw new IllegalArgumentException("password와 password_check가 동일하지 않습니다.");
        }

        // todo email 중복 확인
        // todo nickname 중복 확인

        MemberEntity memberEntity = MemberEntity.from(memberRequest);

        MemberEntity saveMember = memberRepository.save(memberEntity);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MemberResponse.from(saveMember));
    }
}
