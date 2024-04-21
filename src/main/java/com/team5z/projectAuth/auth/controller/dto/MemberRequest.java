package com.team5z.projectAuth.auth.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "닉네임", example = "team5z")
    @Length(min = 3, max = 10, message = "nickname은 3~10글자 입니다.")
    private String nickname;

    @Schema(description = "비밀번호", example = "123456")
    @Length(min = 5, max = 20, message = "password는 5~20글자 입니다.")
    private String password;

    @Schema(description = "비밀번호 확인", example = "123456")
    @Length(min = 5, max = 20, message = "password는 5~20글자 입니다.")
    @NotNull(message = "password_check는 비어있을 수 없습니다.")
    private String passwordCheck;

    public Boolean notEqualPasswordCheck() {
        return !password.equals(passwordCheck);
    }
}
