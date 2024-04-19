package com.team5z.projectAuth.auth.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long memberId;

    @Column(length = 200)
    @Email
    private String email;

    @Column(length = 10)        // 10글자 제한
    private String nickname;

    @Column(length = 200)       // 암호화 되기 때문에 비밀번호 크기가 늘어남
    private String password;    // security 사용해서 암호화 예정
}
