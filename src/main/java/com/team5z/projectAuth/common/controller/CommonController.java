package com.team5z.projectAuth.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth API", description = "Auth API Documentation")
public class CommonController {
    @GetMapping("/health")
    @Operation(summary = "Health Check", description = "api health check")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "정상일 경우 ok 반환", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
    })
    public String health() {
        return "ok";
    }
}
