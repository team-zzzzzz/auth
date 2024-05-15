package com.team5z.projectAuth.auth.controller;

import com.team5z.projectAuth.auth.domain.dto.LoginRequest;
import com.team5z.projectAuth.auth.domain.dto.MemberRequest;
import com.team5z.projectAuth.auth.domain.record.*;
import com.team5z.projectAuth.auth.service.AuthService;
import com.team5z.projectAuth.global.api.ApiCode;
import com.team5z.projectAuth.global.api.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
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
    public ResponseEntity<Response<PostMemberResponse>> join(@RequestBody @Validated MemberRequest memberRequest, BindingResult bindingResult) {
        PostMemberResponse response = authService.join(memberRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.<PostMemberResponse>builder()
                        .code(ApiCode.SUCCESS.getCode())
                        .message(ApiCode.SUCCESS.getMessage())
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
                        .code(ApiCode.SUCCESS.getCode())
                        .message(ApiCode.SUCCESS.getMessage())
                        .data(response)
                        .build()
                );
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
    })
    public ResponseEntity<Response<LoginRecord>> login(@RequestBody @Validated LoginRequest loginRequest, BindingResult bindingResult) {
        LoginRecord response = authService.login(loginRequest);
        return ResponseEntity.ok(
                Response.<LoginRecord>builder()
                        .code(ApiCode.SUCCESS.getCode())
                        .message(ApiCode.SUCCESS.getMessage())
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/refresh/{refresh_token}")
    @Operation(summary = "token 재발급", description = "token 재발급 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
    })
    public ResponseEntity<Response<LoginRecord>> refresh(@PathVariable(name = "refresh_token")
                                                             @NotNull(message = "refresh_token 비어있을 수 없습니다.")
                                                             @Schema(description = "refresh token", example = "refresh token을 입력해주세요.")
                                                             String refreshToken) {
        LoginRecord loginRecord = authService.refresh(refreshToken);
        return ResponseEntity.ok(
                Response.<LoginRecord>builder()
                        .code(ApiCode.SUCCESS.getCode())
                        .message(ApiCode.SUCCESS.getMessage())
                        .data(loginRecord)
                        .build()
        );
    }

    @GetMapping("/biz/{biz_number}")
    @Operation(summary = "사업자 번호 인증", description = "사업자 번호 인증 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상", content = @Content(schema = @Schema(implementation = BizInfoListRecord.class))),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
    })
    public ResponseEntity<Response<BizInfoListRecord>> getBizInfo(@PathVariable(name = "biz_number")
                                                         @NotNull(message = "biz_number 비어있을 수 없습니다.")
                                                         @Schema(description = "biz number", example = "144-81-03460")
                                                         String bizNumber) {
        BizInfoListRecord bizInfo = authService.getBizInfo(bizNumber);
        return ResponseEntity.ok(
            Response.<BizInfoListRecord>builder()
                    .code(ApiCode.SUCCESS.getCode())
                    .message(ApiCode.SUCCESS.getMessage())
                    .data(bizInfo)
                    .build()
        );
    }

    @GetMapping("/biz/mail/{biz_number}")
    @Operation(summary = "통신 판매업 번호 인증", description = "통신 판매업 번호 인증 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상", content = @Content(schema = @Schema(implementation = MailBizRecord.class))),
            @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
    })
    public ResponseEntity<Response<MailBizRecord>> getMailBizInfo(@PathVariable(name = "biz_number")
                                                                  @NotNull(message = "biz_number 비어있을 수 없습니다.")
                                                                  @Schema(description = "biz number", example = "144-81-03460")
                                                                  String bizNumber) {
        MailBizRecord mailBizResponse = authService.getMailBizInfo(bizNumber);
        return ResponseEntity.ok(
                Response.<MailBizRecord>builder()
                        .code(ApiCode.SUCCESS.getCode())
                        .message(ApiCode.SUCCESS.getMessage())
                        .data(mailBizResponse)
                        .build()
        );
    }
}
