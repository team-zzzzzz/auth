package com.team5z.projectAuth.auth.domain.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.team5z.projectAuth.auth.domain.entity.MemberEntity;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PostMemberResponse(
        @JsonProperty("member_id")
        Long memberId,
        List<String> roles
) {


    public static PostMemberResponse from(MemberEntity saveMember) {
        return PostMemberResponse.builder()
                .memberId(saveMember.getMemberId())
                .roles(saveMember.getRole().stream().map(m -> m.getRoles().name()).collect(Collectors.toList()))
                .build();
    }
}
