package com.team5z.projectAuth.auth.service;

import com.team5z.projectAuth.auth.domain.entity.MemberEntity;
import com.team5z.projectAuth.auth.repository.CustomRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final CustomRepositoryImpl customRepositoryImpl;

    public MemberEntity getMember() {
        return customRepositoryImpl.getMember();
    }
}
