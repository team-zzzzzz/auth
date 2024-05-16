package com.team5z.projectAuth.auth.service;

import com.team5z.projectAuth.auth.domain.entity.MemberEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Test
    void getMember() {
        MemberEntity member = memberService.getMember();
        System.out.println("test");
    }

}