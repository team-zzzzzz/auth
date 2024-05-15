package com.team5z.projectAuth.auth.repository;

import com.team5z.projectAuth.auth.domain.entity.MemberRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRoleRepository extends JpaRepository<MemberRoleEntity, Long> {
}
