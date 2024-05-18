package com.team5z.projectAuth.auth.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5z.projectAuth.auth.domain.entity.MemberEntity;
import com.team5z.projectAuth.auth.domain.entity.QMemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomRepositoryImpl implements CustomRepository {
    private final JPAQueryFactory query;

    public MemberEntity getMember() {
        return query.select(QMemberEntity.memberEntity).from(QMemberEntity.memberEntity)
                .where(QMemberEntity.memberEntity.memberId.eq(1L))
                .fetchOne();
    }
}
