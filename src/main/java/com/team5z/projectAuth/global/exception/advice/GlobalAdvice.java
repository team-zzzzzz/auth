package com.team5z.projectAuth.global.exception.advice;

import com.team5z.projectAuth.global.api.ErrorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalAdvice {
    private final Environment env;

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> illegalArgumentException(IllegalArgumentException e) {

        List<ErrorDto> errors = new ArrayList<>();
        errors.add(ErrorDto.builder().message(e.getMessage()).build());

        ProblemDetail problemDetail = createProblemDetail(HttpStatus.BAD_REQUEST, "입력 값을 확인해주세요.", errors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    private ProblemDetail createProblemDetail(HttpStatus httpStatus, String detail, List<ErrorDto> errors) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, detail);
        problemDetail.setProperty("errors", errors);
        problemDetail.setType(URI.create(Optional.ofNullable(env.getProperty("springdoc.swagger-ui.path")).orElse("/api/auth/")));
        return problemDetail;
    }
}
