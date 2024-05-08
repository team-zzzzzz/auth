package com.team5z.projectAuth.auth.domain.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.team5z.projectAuth.auth.domain.entity.MemberEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MemberResponse (
        @JsonProperty("member_id")
        Long memberId
) {


    public static MemberResponse from(MemberEntity saveMember) {
        return MemberResponse.builder()
                .memberId(saveMember.getMemberId())
                .build();
    }
}
