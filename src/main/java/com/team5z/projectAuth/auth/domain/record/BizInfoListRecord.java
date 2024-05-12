package com.team5z.projectAuth.auth.domain.record;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record BizInfoListRecord (
    @JsonProperty("status_code") String statusCode,
    @JsonProperty("match_cnt") int matchCnt,
    @JsonProperty("request_cnt") int requestCnt,
    @JsonProperty("data") List<BizInfoRecord> data
){}
