package com.team5z.projectAuth.global.aop;

import com.team5z.projectAuth.global.api.ErrorDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class BadRequestAspect {
    private final Environment env;

    // ProceedingJoinPoint 사용을 위해 @Around 사용
    @Around("execution(* com.team5z.projectAuth..*.controller..*.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        String type = pjp.getSignature().getDeclaringTypeName();
        String method = pjp.getSignature().getName();
        String requestURI = ((ServletRequestAttributes) requestAttributes).getRequest()
                .getRequestURI();
        String docs = Optional.ofNullable(env.getProperty("springdoc.swagger-ui.path")).orElse("/api/auth/");

        log.info("[bad request] validation check... requestUri=[{}] package = [{}], method = [{}]",
                requestURI, type, method);

        Object[] args = pjp.getArgs();
        for (Object a : args) {
            // 유효성 검사에 걸리는 에러가 존재한다면
            if (a instanceof BindingResult bindingResult
                    && bindingResult.hasErrors()) {   // object type == BindingResult
                List<ErrorDto> errors = new ArrayList<>();
                for (FieldError error : bindingResult.getFieldErrors()) {
                    errors.add(
                            ErrorDto.builder()
                                    .message(error.getDefaultMessage())
                                    .build()
                    );
                }

                ProblemDetail pb = ProblemDetail.forStatusAndDetail(
                        HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), "잘못된 입력입니다.");
                pb.setInstance(URI.create(requestURI));
                pb.setType(URI.create(docs));
                pb.setTitle(HttpStatus.BAD_REQUEST.name());
                pb.setProperty("errors", errors);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(pb);
            }
        }

        return pjp.proceed();
    }

}
