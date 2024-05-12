package com.team5z.projectAuth.auth.domain.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "사업자 정보 Response")
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record BizInfoRecord (
        @JsonProperty("b_no") @Schema(description = "사업자 번호", example = "1448103460") String bizNumber,
        @JsonProperty("b_stt") @Schema(description = "납세자상태", example = "계속사업자") String bizStatus,
        @JsonProperty("b_stt_cd") @Schema(description = "납세자상태 코드", example = "01") String bizStatusCode, //
        @JsonProperty("tax_type") @Schema(description = "과세유형메세지", example = "부가가치세 일반과세자") String taxType,
        @JsonProperty("tax_type_cd") @Schema(description = "과세유형메세지 코드", example = "01") String taxTypeCode,
        @JsonProperty("end_dt") @Schema(description = "폐업일 (YYYYMMDD 포맷)", example = "") String endDateAsString,
        @JsonProperty("utcc_yn") @Schema(description = "단위과세전환폐업여부", example = "N") String utccYn,
        @JsonProperty("tax_type_change_dt") @Schema(description = "최근과세유형전환일자 (YYYYMMDD 포맷)", example = "") String taxTypeChangeDt,
        @JsonProperty("invoice_apply_dt") @Schema(description = "세금계산서적용일자 (YYYYMMDD 포맷)", example = "") String invoiceApplyDt,
        @JsonProperty("rbf_tax_type") @Schema(description = "직전과세유형메세지", example = "해당없음") String rbfTaxType,
        @JsonProperty("rbf_tax_type_cd") @Schema(description = "직전과세유형메세지 코드", example = "99") String rbfTaxTypeCode
){}
