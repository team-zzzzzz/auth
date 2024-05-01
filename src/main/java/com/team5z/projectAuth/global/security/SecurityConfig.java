package com.team5z.projectAuth.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team5z.projectAuth.global.security.apply.AuthFilter;
import com.team5z.projectAuth.global.security.apply.TokenProvider;
import com.team5z.projectAuth.global.security.exception.MyAccessDeniedHandler;
import com.team5z.projectAuth.global.security.exception.MyEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final MyEntryPoint myEntryPoint;
    private final MyAccessDeniedHandler myAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception{
        return security.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .headers(c -> c.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable).disable())   // h2 설정
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(
                        e -> e.authenticationEntryPoint(myEntryPoint)
                                .accessDeniedHandler(myAccessDeniedHandler)
                ) // 인증 예외처리
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }
}
