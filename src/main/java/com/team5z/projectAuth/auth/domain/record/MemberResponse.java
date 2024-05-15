package com.team5z.projectAuth.auth.domain.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.team5z.projectAuth.auth.domain.entity.MemberEntity;
import com.team5z.projectAuth.auth.domain.entity.MemberStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MemberResponse(
        @JsonProperty("member_id") @Schema(description = "회원번호", example = "1")
        Long memberId,
        @Schema(description = "email", example = "team5z@mail.com")
        String email,
        @Schema(description = "연락처", example = "01012345678")
        String tel,
        @Schema(description = "이름", example = "홍길동")
        String name,
        @JsonProperty("zip_code") @Schema(description = "우편주소", example = "12345")
        String zipCode,
        @Schema(description = "사업장 주소", example = "경기도 성남시 분당구 판교로")
        String address,
        @Schema(description = "사업장 상세 주소", example = "판교역 지하 1층")
        String addressDetail,
        @Schema(description = "회원 상태", example = "ACTIVE")
        String status,
        @Schema(description = "회원 type", example = "EMAIL_SELLER")
        String type,
        @JsonProperty("biz_number") @Schema(description = "사업자 번호", example = "1448103460")
        String bizNumber,
        @JsonProperty("biz_mail_number") @Schema(description = "통신판매업 번호", example = "2024경기성남01234")
        String bizMailNumber,
        @JsonProperty("modified_at") @Schema(description = "수정일", example = "2024-05-15T 13:00:00")
        LocalDateTime modifiedAt,
        @JsonProperty("created_at") @Schema(description = "가입일", example = "2024-05-15T 12:00:00")
        LocalDateTime createdAt
) {
    public static MemberResponse from(MemberEntity member) {
        return MemberResponse.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .tel(member.getTel())
                .name(member.getName())
                .zipCode(member.getZipCode())
                .address(member.getAddress())
                .addressDetail(member.getAddressDetail())
                .status(member.getStatus().name())
                .type(member.getType().name())
                .bizNumber(member.getBizNumber())
                .bizMailNumber(member.getBizMailNumber())
                .modifiedAt(member.getModifiedAt())
                .createdAt(member.getCreatedAt())
                .build();
    }
}
