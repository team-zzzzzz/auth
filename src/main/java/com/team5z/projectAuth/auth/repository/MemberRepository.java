package com.team5z.projectAuth.auth.repository;

import com.team5z.projectAuth.auth.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
}
