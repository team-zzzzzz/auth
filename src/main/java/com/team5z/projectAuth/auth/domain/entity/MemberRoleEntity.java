package com.team5z.projectAuth.auth.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "member_role")
public class MemberRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long roleId;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity member;
    @Enumerated(EnumType.STRING)
    private MemberRoles roles;
}
