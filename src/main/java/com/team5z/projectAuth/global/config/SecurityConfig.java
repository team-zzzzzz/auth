package com.team5z.projectAuth.global.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception{
        return security.csrf(c -> c.disable())
                .cors(c -> c.disable())
                .headers(c -> c.frameOptions(f -> f.disable()).disable())   // h2 설정
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(PathRequest.toH2Console()).permitAll()
                            .anyRequest().permitAll();
                }).sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .exceptionHandling(e -> e.authenticationEntryPoint(new CustomAuthenticationEntryPoint())) // 인증 예외처리
//                .apply()  // 인증처리
                .build();
    }
}
