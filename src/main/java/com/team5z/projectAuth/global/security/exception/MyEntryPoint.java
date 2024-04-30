package com.team5z.projectAuth.global.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team5z.projectAuth.global.api.ErrorDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
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
public class MyEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;
    private final Environment env;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String docs = Optional.ofNullable(env.getProperty("springdoc.swagger-ui.path")).orElse("/api/auth/");
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;

        List<ErrorDto> errors = new ArrayList<>();
        errors.add(ErrorDto.builder().message("please check request token").build());

        ProblemDetail pb = ProblemDetail.forStatusAndDetail(httpStatus, "FORBIDDEN");
        pb.setType(URI.create(docs));
        pb.setProperty("errors", errors);
        pb.setInstance(URI.create(request.getRequestURI()));

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());
        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(pb));
    }
}
