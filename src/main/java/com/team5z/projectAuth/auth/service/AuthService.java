package com.team5z.projectAuth.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team5z.projectAuth.auth.domain.dto.LoginRequest;
import com.team5z.projectAuth.auth.domain.dto.MemberRequest;
import com.team5z.projectAuth.auth.domain.dto.TokenSaveDto;
import com.team5z.projectAuth.auth.domain.entity.MemberRoleEntity;
import com.team5z.projectAuth.auth.domain.record.*;
import com.team5z.projectAuth.auth.domain.entity.MemberEntity;
import com.team5z.projectAuth.auth.repository.MemberRepository;
import com.team5z.projectAuth.auth.repository.MemberRoleRepository;
import com.team5z.projectAuth.global.security.apply.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthService {
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final Environment env;
    private final RestClient restClient;

    @Transactional
    public PostMemberResponse join(MemberRequest memberRequest) {
        if (memberRequest.notEqualPasswordCheck()) {
            throw new IllegalArgumentException("password와 password_check가 동일하지 않습니다.");
        }

        // email 중복 확인
        Optional<MemberEntity> findMember = memberRepository.findByEmail(memberRequest.getEmail());
        if (findMember.isPresent()) {
            throw new IllegalArgumentException("이미 가입된 email 입니다.");
        }

        MemberEntity memberEntity = MemberEntity.from(memberRequest, passwordEncoder);
        List<MemberRoleEntity> role = memberEntity.createRole(memberRequest);

        memberRoleRepository.saveAll(role);
        MemberEntity saveMember = memberRepository.save(memberEntity);
        return PostMemberResponse.from(saveMember);
    }

    public MessageRecord findMemberByEmail(String email) {
        Optional<MemberEntity> findMember = memberRepository.findByEmail(email);
        if (findMember.isPresent()) {
            throw new IllegalArgumentException("이미 가입된 email 입니다.");
        }
        return MessageRecord.builder()
                .message("정상")
                .build();
    }

    public LoginRecord login(LoginRequest loginRequest) {
        // id, pw 기반으로 UsernamePasswordAuthenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();
        // security에 구현한 MyUserDetailsService 실행됨
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        LoginRecord loginRecord = tokenProvider.createToken(authenticate);
        TokenSaveDto tokenSaveDto = TokenSaveDto.from(authenticate);

        // token redis 저장
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();

        try {
            ops.set(loginRecord.refreshToken(), objectMapper.writeValueAsString(tokenSaveDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("tokenSaveDto convert string fail", e);
        }

        redisTemplate.expireAt(loginRecord.refreshToken(), Instant.ofEpochSecond(loginRecord.refreshTokenExpired()));

        return loginRecord;
    }

    public LoginRecord refresh(String refreshToken) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        String tokenSaveAsString = Optional.ofNullable(ops.get(refreshToken)).orElse("").toString();
        if (tokenSaveAsString.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 refresh token 입니다.");
        }

        TokenSaveDto tokenSaveDto = null;
        try {
            tokenSaveDto = objectMapper.readValue(tokenSaveAsString, TokenSaveDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("tokenSaveDto convert dto fail", e);
        }
        LoginRecord loginRecord = tokenProvider.createToken(tokenSaveDto);
        redisTemplate.delete(refreshToken);

        try {
            ops.set(loginRecord.refreshToken(), objectMapper.writeValueAsString(tokenSaveDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("tokenSaveDto convert string fail", e);
        }
        redisTemplate.expireAt(loginRecord.refreshToken(), Instant.ofEpochSecond(loginRecord.refreshTokenExpired()));
        return loginRecord;
    }

    public BizInfoListRecord getBizInfo(String bizNumber) {
        Map<String, Object> map = new HashMap<>();
        List<String> bizNumberList = new ArrayList<>();
        bizNumberList.add(bizNumber.replaceAll("-", ""));
        map.put("b_no", bizNumberList);
        String key = Optional.ofNullable(env.getProperty("api.biz")).orElse("");
        String url = String.format("https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=%s", key);
        // uri의 파라미터의 경우 uri로 감싸줘야 제대로 전송됨.
        URI uri = URI.create(url);

        ResponseEntity<BizInfoListRecord> entity = restClient.post().uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(map)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    HttpStatusCode statusCode = response.getStatusCode();
                    InputStream body = response.getBody();
                    String bodyAsString = "";
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(body));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                        bodyAsString = stringBuilder.toString();
                    } finally {
                        // 작업을 마치면 InputStream을 닫아줍니다.
                        body.close();
                    }

                    throw new RuntimeException(String.format("[%s] [%s]", statusCode, bodyAsString));
                })
                .toEntity(BizInfoListRecord.class);

        if (entity.getStatusCode() == HttpStatus.OK) {
            BizInfoListRecord body = entity.getBody();
            System.out.println(body);
        }
        return entity.getBody();
    }

    public MailBizRecord getMailBizInfo(String bizNumber) {
        String key = Optional.ofNullable(env.getProperty("api.biz")).orElse("");
        String url = String.format("https://apis.data.go.kr/1130000/MllBsDtl_1Service/getMllBsInfoDetail_1?serviceKey=%s", key);
        String query = String.format("&pageNo=1&numOfRows=1&resultType=json&brno=%s", bizNumber.replaceAll("-", ""));
        URI uri = URI.create(url + query);

        ResponseEntity<MailBizResponse> response = restClient.get().uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(MailBizResponse.class);

        System.out.println("test");
        return MailBizRecord.from(response.getBody().items().getFirst());
    }

    public MemberResponse getMember(String accessToken) {
        Long memberId = tokenProvider.getMemberId(accessToken);
        MemberEntity member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("회원의 정보가 없습니다."));
        return MemberResponse.from(member);
    }
}
