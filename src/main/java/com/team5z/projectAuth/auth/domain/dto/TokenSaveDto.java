package com.team5z.projectAuth.auth.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;

import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenSaveDto {
    private Long memberId;
    private String role;

    public static TokenSaveDto from(Authentication authenticate) {
        return TokenSaveDto.builder()
                .memberId(Long.parseLong(authenticate.getName()))
                .role(authenticate.getAuthorities().stream().map(String::valueOf).collect(Collectors.joining(",")))
                .build();
    }
}
