package com.team5z.projectAuth.auth.service;

import com.team5z.projectAuth.auth.domain.dto.MemberRequest;
import com.team5z.projectAuth.auth.domain.entity.MemberEntity;
import com.team5z.projectAuth.auth.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void getMember() {
        //given
        MemberRequest request = MemberRequest.builder()
                .email("mail@mail.com")
                .name("최준호")
                .address("주소")
                .tel("01012345678")
                .addressDetail("상세주소")
                .zipCode("1234")
                .bizNumber("12345678")
                .type("SELLER")
                .password("123456")
                .passwordCheck("123456")
                .nickname("닉네임")
                .build();
        memberRepository.save(MemberEntity.from(request, bCryptPasswordEncoder));

        //when
        MemberEntity member = memberService.getMember();

        //then
        Assertions.assertThat(member).isNotNull();
    }

}