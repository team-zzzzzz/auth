package com.team5z.projectAuth.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team5z.projectAuth.global.api.ErrorDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    private final Environment env;
    private final ObjectMapper objectMapper;
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String docs = Optional.ofNullable(env.getProperty("springdoc.swagger-ui.path")).orElse("/api/auth/");

        List<ErrorDto> errors = new ArrayList<>();
        errors.add(ErrorDto.builder().message("unauthorized token").build());

        ProblemDetail pb = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
        pb.setType(URI.create(docs));
        pb.setProperty("errors", errors);
        pb.setInstance(URI.create(request.getRequestURI()));

        PrintWriter writer = response.getWriter();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        writer.write(objectMapper.writeValueAsString(pb));
    }
}
