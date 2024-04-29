package com.team5z.projectAuth.auth.service;

import com.team5z.projectAuth.auth.controller.dto.MemberRequest;
import com.team5z.projectAuth.auth.controller.dto.MemberResponse;
import com.team5z.projectAuth.auth.controller.record.MessageRecord;
import com.team5z.projectAuth.auth.domain.entity.MemberEntity;
import com.team5z.projectAuth.auth.repository.MemberRepository;
import com.team5z.projectAuth.global.api.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final MemberRepository memberRepository;
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

        MemberEntity memberEntity = MemberEntity.from(memberRequest);

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
}
