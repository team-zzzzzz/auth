package com.team5z.projectAuth.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team5z.projectAuth.global.config.apply.AuthFilter;
import com.team5z.projectAuth.global.config.apply.JwtSecurityConfig;
import com.team5z.projectAuth.global.config.apply.TokenProvider;
import com.team5z.projectAuth.global.config.exception.MyAccessDeniedHandler;
import com.team5z.projectAuth.global.config.exception.MyEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final MyEntryPoint myEntryPoint;
    private final MyAccessDeniedHandler myAccessDeniedHandler;
    private final TokenProvider tokenProvider;
    private static final String[] DEFAULT_LIST = {
            "/api/auth/swagger-ui/index.html"
    };

    private static final String[] WHITE_LIST = {
            "/api/auth/**"
    };

    private static final String[] SELLER_LIST = {
            "/api/auth/test"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security, ObjectMapper objectMapper, Environment env) throws Exception{
        return security.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .headers(c -> c.frameOptions(f -> f.disable()).disable())   // h2 설정
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(
                        e -> e.authenticationEntryPoint(myEntryPoint)
                                .accessDeniedHandler(myAccessDeniedHandler)
                ) // 인증 예외처리
                .addFilterBefore(new AuthFilter(tokenProvider, env, objectMapper), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers(SELLER_LIST).hasRole("SELLER")
                            .requestMatchers(PathRequest.toH2Console()).permitAll()
                            .requestMatchers(DEFAULT_LIST).permitAll()
                            .anyRequest().permitAll();
                })
                .build();
    }

}
