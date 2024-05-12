package com.team5z.projectAuth.global.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ApiCode {

    SUCCESS("0000", "정상"),
    FAIL("9999", "서버 에러")
    ;
    private final String code;
    private final String message;
}
