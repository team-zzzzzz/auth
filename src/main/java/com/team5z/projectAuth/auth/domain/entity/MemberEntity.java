package com.team5z.projectAuth.auth.domain.entity;

import com.team5z.projectAuth.auth.controller.dto.MemberRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "member")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long memberId;

    @Column(length = 200, unique = true)
    @Email
    private String email;

    @Column(length = 10, unique = true)        // 10글자 제한
    private String nickname;

    @Column(length = 200)       // 암호화 되기 때문에 비밀번호 크기가 늘어남
    private String password;    // security 사용해서 암호화 예정

    public static MemberEntity from(MemberRequest memberRequest) {
        return MemberEntity.builder()
                .email(memberRequest.getEmail())
                .nickname(memberRequest.getNickname())
                .password(memberRequest.getPassword())
                .build();
    }
}
