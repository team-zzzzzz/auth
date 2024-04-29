package com.team5z.projectAuth.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final MyEntryPoint myEntryPoint;
    private final MyAccessDeniedHandler myAccessDeniedHandler;
    private static final String[] DEFAULT_LIST = {
            "/api/auth/swagger-ui/index.html"
    };

    private static final String[] WHITE_LIST = {
            "/api/auth/**"
    };

    private static final String[] AUTH_LIST = {
            "/api/auth/test"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security, ObjectMapper objectMapper, Environment env) throws Exception{
        return security.csrf(c -> c.disable())
                .cors(c -> c.disable())
                .headers(c -> c.frameOptions(f -> f.disable()).disable())   // h2 설정
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers(AUTH_LIST).hasRole("SELLER")
                            .requestMatchers(PathRequest.toH2Console()).permitAll()
                            .anyRequest().permitAll();
                }).sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(
                        e -> e.authenticationEntryPoint(myEntryPoint)
                                .accessDeniedHandler(myAccessDeniedHandler)
                ) // 인증 예외처리
//                .apply()  // 인증처리
                .build();
    }
}
