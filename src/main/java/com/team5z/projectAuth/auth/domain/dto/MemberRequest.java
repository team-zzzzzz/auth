package com.team5z.projectAuth.auth.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberRequest {
    @Schema(description = "가입 이메일", example = "team5z@mail.com")
    @Email(message = "email 형식을 확인해 주세요.")
    private String email;

    @Schema(description = "이름", example = "홍길동")
    @Length(min = 3, max = 10, message = "name 3~10글자 입니다.")
    @NotNull(message = "name 비어있을 수 없습니다.")
    private String name;

    @Schema(description = "연락처", example = "01012345678")
    @Length(min = 11, max = 11, message = "tel 11글자 입니다.")
    @NotNull(message = "tel 비어있을 수 없습니다.")
    private String tel;

    @Schema(description = "판매자들에게 노출될 닉네임", example = "team5z")
    @Length(min = 3, max = 10, message = "nickname 3~10글자 입니다.")
    @NotNull(message = "nickname 비어있을 수 없습니다.")
    private String nickname;

    @Schema(description = "비밀번호", example = "123456")
    @Length(min = 5, max = 20, message = "password는 5~20글자 입니다.")
    @NotNull(message = "password 비어있을 수 없습니다.")
    private String password;

    @Schema(description = "비밀번호 확인", example = "123456")
    @Length(min = 5, max = 20, message = "password는 5~20글자 입니다.")
    @NotNull(message = "password_check 비어있을 수 없습니다.")
    private String passwordCheck;

    @Schema(description = "사업자 번호", example = "1448103460")
    @Length(min = 10, max = 10, message = "biz_number 10글자 입니다.")
    @NotNull(message = "biz_number 비어있을 수 없습니다.")
    private String bizNumber;

    @Schema(description = "사업자 번호", example = "1448103460")
    @Length(min = 10, max = 10, message = "biz_number 10글자 입니다.")
    @Nullable
    private String mailBizNumber;

    public Boolean notEqualPasswordCheck() {
        return !password.equals(passwordCheck);
    }
}
