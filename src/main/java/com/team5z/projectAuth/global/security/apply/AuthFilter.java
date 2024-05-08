package com.team5z.projectAuth.global.security.apply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team5z.projectAuth.global.api.ErrorDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@Slf4j
public class AuthFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 인증처리
        String token = request.getHeader(AUTHORIZATION);
        Authentication authentication = tokenProvider.getAuthentication(token);

        String role = authentication.getAuthorities().stream().map(String::valueOf).collect(Collectors.joining(","));
        request.setAttribute("memberId", authentication.getName());
        request.setAttribute("role", role);
        filterChain.doFilter(request, response);
    }
}
