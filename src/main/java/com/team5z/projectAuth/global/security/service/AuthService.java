package com.team5z.projectAuth.global.security.service;

import com.team5z.projectAuth.auth.domain.entity.MemberEntity;
import com.team5z.projectAuth.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        MemberEntity findMember = memberRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("유효하지 않은 회원입니다.")
        );

        // todo 멤버 권한 설정해줘야 함.
        String[] role = {"SELLER"};

        return User.builder()
                .username(String.valueOf(findMember.getMemberId()))
                .password(findMember.getPassword())
                .roles(role)
                .build();
    }
}
