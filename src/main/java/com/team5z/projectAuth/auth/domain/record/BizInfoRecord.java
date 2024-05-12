package com.team5z.projectAuth.auth.domain.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record BizInfoRecord (
        @JsonProperty("b_no") String bizNumber,
        @JsonProperty("b_stt") String bizStatus,
        @JsonProperty("b_stt_cd") String bizStatusCode,
        @JsonProperty("tax_type") String taxType,
        @JsonProperty("tax_type_cd") String taxTypeCode,
        @JsonProperty("end_dt") String endDateAsString
){}
