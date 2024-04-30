package com.team5z.projectAuth.auth.controller;

import com.team5z.projectAuth.auth.controller.dto.LoginRequest;
import com.team5z.projectAuth.auth.controller.dto.MemberRequest;
import com.team5z.projectAuth.auth.controller.dto.MemberResponse;
import com.team5z.projectAuth.auth.controller.record.LoginRecord;
import com.team5z.projectAuth.auth.controller.record.MessageRecord;
import com.team5z.projectAuth.auth.service.AuthService;
import com.team5z.projectAuth.global.api.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth API", description = "Auth API Documentation")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/join")
    @Operation(summary = "회원가입", description = "회원가입 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "정상", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
    })
    public ResponseEntity<Response<MemberResponse>> join(@RequestBody @Validated MemberRequest memberRequest, BindingResult bindingResult) {
        MemberResponse response = authService.join(memberRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.<MemberResponse>builder()
                        .code("0000")
                        .message("정상")
                        .data(response)
                        .build()
                );
    }

    @GetMapping("/{email}")
    @Operation(summary = "회원가입 email 중복 체크", description = "회원가입 email 중복 체크")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
    })
    public ResponseEntity<Response<MessageRecord>> findMemberByEmail(@PathVariable(name = "email") @Schema(description = "가입 이메일", example = "team5z@mail.com") String email) {
        MessageRecord response = authService.findMemberByEmail(email);
        return ResponseEntity.ok(
                Response.<MessageRecord>builder()
                        .code("0000")
                        .message("정상")
                        .data(response)
                        .build()
                );
    }

    @PostMapping("/login")
    public ResponseEntity<Response<LoginRecord>> login(@RequestBody @Validated LoginRequest loginRequest, BindingResult bindingResult) {
        LoginRecord response = authService.login(loginRequest);
        return ResponseEntity.ok(
                Response.<LoginRecord>builder()
                        .code("0000")
                        .message("정상")
                        .data(response)
                        .build()
        );
    }
}
