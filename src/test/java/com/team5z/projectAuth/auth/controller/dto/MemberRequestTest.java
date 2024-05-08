package com.team5z.projectAuth.auth.controller.dto;

import com.team5z.projectAuth.auth.domain.dto.MemberRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


class MemberRequestTest {

    @Test
    void notEqualPasswordCheck() {
        // given
        MemberRequest memberRequest = new MemberRequest();
        memberRequest.setPassword("1234");
        memberRequest.setPasswordCheck("12345");

        // when
        Boolean result = memberRequest.notEqualPasswordCheck();
        
        // then
        Assertions.assertThat(result).isTrue();
    }

}