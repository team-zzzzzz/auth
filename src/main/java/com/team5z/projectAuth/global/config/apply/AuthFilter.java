package com.team5z.projectAuth.global.config.apply;

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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@Slf4j
public class AuthFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final Environment env;
    private final ObjectMapper objectMapper;
    private final String AUTH_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 인증처리
        String token = request.getHeader(AUTHORIZATION);
        if (token != null && token.startsWith(AUTH_PREFIX)) {
            String docs = Optional.ofNullable(env.getProperty("springdoc.swagger-ui.path")).orElse("/api/auth/");
            List<ErrorDto> errors = new ArrayList<>();
            errors.add(ErrorDto.builder().message(String.format("token is start with `%`", AUTH_PREFIX)).build());

            ProblemDetail pb = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
            pb.setType(URI.create(docs));
            pb.setProperty("errors", errors);
            pb.setInstance(URI.create(request.getRequestURI()));

            PrintWriter writer = response.getWriter();
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            writer.write(objectMapper.writeValueAsString(pb));
            return ;
        }

        filterChain.doFilter(request, response);
    }
}
