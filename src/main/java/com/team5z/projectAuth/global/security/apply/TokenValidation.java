package com.team5z.projectAuth.global.security.apply;

public enum TokenValidation {
    SUCCESS_JWT,
    MALFORMED_JWT,
    EXPIRED_JWT,
    UNSUPPORTED_JWT,
    ILLEGAL_ARGUMENT_JWT,
}
