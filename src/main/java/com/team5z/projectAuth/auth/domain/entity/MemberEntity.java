package com.team5z.projectAuth.auth.domain.entity;

import com.team5z.projectAuth.auth.domain.dto.MemberRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Column(length = 200)       // 암호화 되기 때문에 비밀번호 크기가 늘어남
    private String password;    // security 사용해서 암호화 예정

    @Column(length = 11)
    private String tel;
    private String name;
    private String nickname;
    private String zipCode;
    private String address;
    private String addressDetail;
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Enumerated(EnumType.STRING)
    private MemberType type;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<MemberRoleEntity> role;

    @Column(length = 11)
    private String bizNumber;
    @Column(length = 13)
    private String bizMailNumber;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.modifiedAt = now;
    }
    @PreUpdate
    public void preUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }
    public static MemberEntity from(MemberRequest memberRequest, BCryptPasswordEncoder passwordEncoder) {
        MemberEntity member = MemberEntity.builder()
                .email(memberRequest.getEmail())
                .password(passwordEncoder.encode(memberRequest.getPassword()))
                .tel(memberRequest.getTel().replaceAll("-", ""))
                .name(memberRequest.getName())
                .nickname(memberRequest.getNickname())
                .zipCode(memberRequest.getZipCode())
                .address(memberRequest.getAddress())
                .addressDetail(memberRequest.getAddressDetail())
                .bizNumber(memberRequest.getBizNumber().replaceAll("-", ""))
                .status(MemberStatus.ACTIVE)
                .type(MemberType.valueOf(memberRequest.getType()))
                .build();

        if (member.type == MemberType.EMAIL_SELLER) {
            Optional.ofNullable(memberRequest.getMailBizNumber()).orElseThrow(() ->
                    new IllegalArgumentException("type이 EMAIL_SELLER일 경우 biz_mail_number 값이 필수입니다.")
            );
            member.bizMailNumber = memberRequest.getMailBizNumber().replaceAll("-", "");
        }

        return member;
    }

    public List<MemberRoleEntity> createRole(MemberRequest memberRequest) {
        List<MemberRoleEntity> roleList = new ArrayList<>();
        roleList.add(MemberRoleEntity.builder().member(this).roles(MemberRoles.SELLER).build());
        if (Optional.ofNullable(memberRequest.getMailBizNumber()).isPresent()) {
            roleList.add(MemberRoleEntity.builder().member(this).roles(MemberRoles.MAIL_SELLER).build());
        }
        this.role = roleList;
        return roleList;
    }
}
