package com.team5z.projectAuth.auth.domain.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;


@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MailBizRecord (
        @JsonProperty("opn_sn") @Schema(description = "일련번호", example = "887621978069799300")
        long opnSn,
        @JsonProperty("prmmi_yr") @Schema(description = "인허가번호(연도)", example = "2012")
        String prmmiYr,
        @JsonProperty("prmmi_mnno") @Schema(description = "인허가 관리번호", example = "2012-경기성남-1139")
        String prmmiMnno,
        @JsonProperty("ctpv_nm") @Schema(description = "시도명", example = "경기도")
        String ctpvNm,
        @JsonProperty("dclr_inst_nm") @Schema(description = "신고기관지역명", example = "N/A")
        String dclrInstNm,
        @JsonProperty("oper_sttus_cd_nm") @Schema(description = "운영상태코드명", example = "정상영업")
        String operSttusCdNm,
        @JsonProperty("smtx_trgt_yn_cn") @Schema(description = "간이과세대상자여부내용", example = "비대상")
        String smtxTrgtYnCn,
        @JsonProperty("corp_yn_nm") @Schema(description = "법인여부명", example = "법인")
        String corpYnNm,
        @JsonProperty("bzmn_nm") @Schema(description = "법인명", example = "주식회사 카카오브이엑스(KAKAO VX)")
        String bzmnNm,
        @JsonProperty("crno") @Schema(description = "법인등록번호", example = "1311110318343")
        String crno,
        @JsonProperty("brno") @Schema(description = "사업자등록번호", example = "1448103460")
        String brno,
        @JsonProperty("telno") @Schema(description = "전화번호", example = "16661538")
        String telno,
        @JsonProperty("fxno") @Schema(description = "팩스번호", example = "N/A")
        String fxno,
        @JsonProperty("lctn_rn_addr") @Schema(description = "소재지도로명주소", example = "경기도 성남시 분당구 판교역로")
        String lctnRnAddr,
        @JsonProperty("lctn_addr") @Schema(description = "소재지주소", example = "경기도 성남시 분당구 삼평동")
        String lctnAddr,
        @JsonProperty("domn_cn") @Schema(description = "도메인명", example = "www.maumgolf.com")
        String domnCn,
        @JsonProperty("opn_server_place_aladr") @Schema(description = "호스트서버주소", example = "N/A")
        String opnServerPlaceAladr,
        @JsonProperty("ntsl_mthd_nm") @Schema(description = "판매방식명", example = "02")
        String ntslMthdNm,
        @JsonProperty("ntsl_mthd_cn") @Schema(description = "판매방식내용", example = "인터넷")
        String ntslMthdCn,
        @JsonProperty("trtmnt_prdlst_nm") @Schema(description = "취급품목", example = "02 06 07 12 08")
        String trtmntPrdlstNm,
        @JsonProperty("ntsl_prdlst_cn") @Schema(description = "취급품목설명", example = "교육/도서/완구/오락 의류/패션/잡화/뷰티 레저/여행/공연 기타 건강/식품")
        String ntslPrdlstCn,
        @JsonProperty("dclr_cn") @Schema(description = "신고내용", example = "골프인터넷강의")
        String dclrCn,
        @JsonProperty("chg_cn") @Schema(description = "변경내용", example = "인터넷도메인명 변경소재지 변경")
        String chgCn,
        @JsonProperty("chg_rsn_cn") @Schema(description = "변경사유", example = "대표자변경(2014.6.11)소재지대표자전화번호도메인 변경(2017.06.02.)상호명 전자우편 변경(2017.12.13.)소재지 변경(2018.06.29.)2020.06.10. 인터넷도메인명 변경2022.10.12. 소재지 변경(경기도 성남시 분당구 판교로****** 판교세븐벤처밸리2 ***40140***(삼평동))")
        String chgRsnCn,
        @JsonProperty("tcbiz_bgng_date") @Schema(description = "휴업시작일자", example = "N/A")
        String tcbizBgngDate,
        @JsonProperty("tcbiz_end_date") @Schema(description = "휴업종료일자", example = "N/A")
        String tcbizEndDate,
        @JsonProperty("clsbiz_date") @Schema(description = "폐업일자", example = "N/A")
        String clsbizDate,
        @JsonProperty("bsn_resmpt_date") @Schema(description = "영업재개일자", example = "N/A")
        String bsnResmptDate,
        @JsonProperty("spcss_rsn_cn") @Schema(description = "휴폐업사유", example = " ")
        String spcssRsnCn,
        @JsonProperty("dclr_date") @Schema(description = "신고일자", example = "20120724")
        String dclrDate,
        @JsonProperty("lctn_rn_ozip") @Schema(description = "도로명_우편번호", example = "13494")
        String lctnRnOzip,
        @JsonProperty("rn_addr") @Schema(description = "도로명주소", example = "경기도 성남시 분당구 판교역로")
        String rnAddr,
        @JsonProperty("opn_mdfcn_dt") @Schema(description = "최종수정시점", example = "20230105000000")
        String opnMdfcnDt,
        @JsonProperty("prcs_dept_dtl_nm") @Schema(description = "처리부서", example = "N/A")
        String prcsDeptDtlNm,
        @JsonProperty("prcs_dept_area_nm") @Schema(description = "처리부서지역명", example = "N/A")
        String prcsDeptAreaNm,
        @JsonProperty("prcs_dept_nm") @Schema(description = "처리부서명", example = "N/A")
        String prcsDeptNm,
        @JsonProperty("chrg_dept_telno") @Schema(description = "관리부서전화번호", example = "N/A")
        String chrgDeptTelno,
        @JsonProperty("rprsv_nm") @Schema(description = "대표자명", example = "N/A")
        String rprsvNm,
        @JsonProperty("rprsv_emladr") @Schema(description = "대표자전자우편", example = "N/A")
        String rprsvEmladr
) {
        public static MailBizRecord from(MailBizVo mailBizVo) {
                return MailBizRecord.builder()
                        .opnSn(mailBizVo.opnSn())
                        .prmmiYr(mailBizVo.prmmiYr())
                        .prmmiMnno(mailBizVo.prmmiMnno())
                        .ctpvNm(mailBizVo.ctpvNm())
                        .dclrInstNm(mailBizVo.dclrInstNm())
                        .operSttusCdNm(mailBizVo.operSttusCdNm())
                        .smtxTrgtYnCn(mailBizVo.smtxTrgtYnCn())
                        .corpYnNm(mailBizVo.corpYnNm())
                        .bzmnNm(mailBizVo.bzmnNm())
                        .crno(mailBizVo.crno())
                        .brno(mailBizVo.brno())
                        .telno(mailBizVo.telno())
                        .fxno(mailBizVo.fxno())
                        .lctnRnAddr(mailBizVo.lctnRnAddr())
                        .lctnAddr(mailBizVo.lctnAddr())
                        .domnCn(mailBizVo.domnCn())
                        .opnServerPlaceAladr(mailBizVo.opnServerPlaceAladr())
                        .ntslMthdNm(mailBizVo.ntslMthdNm())
                        .ntslMthdCn(mailBizVo.ntslMthdCn())
                        .trtmntPrdlstNm(mailBizVo.trtmntPrdlstNm())
                        .ntslPrdlstCn(mailBizVo.ntslPrdlstCn())
                        .dclrCn(mailBizVo.dclrCn())
                        .chgCn(mailBizVo.chgCn())
                        .chgRsnCn(mailBizVo.chgRsnCn())
                        .tcbizBgngDate(mailBizVo.tcbizBgngDate())
                        .tcbizEndDate(mailBizVo.tcbizEndDate())
                        .clsbizDate(mailBizVo.clsbizDate())
                        .bsnResmptDate(mailBizVo.bsnResmptDate())
                        .spcssRsnCn(mailBizVo.spcssRsnCn())
                        .dclrDate(mailBizVo.dclrDate())
                        .lctnRnOzip(mailBizVo.lctnRnOzip())
                        .rnAddr(mailBizVo.rnAddr())
                        .opnMdfcnDt(mailBizVo.opnMdfcnDt())
                        .prcsDeptDtlNm(mailBizVo.prcsDeptDtlNm())
                        .prcsDeptAreaNm(mailBizVo.prcsDeptAreaNm())
                        .prcsDeptNm(mailBizVo.prcsDeptNm())
                        .chrgDeptTelno(mailBizVo.chrgDeptTelno())
                        .rprsvNm(mailBizVo.rprsvNm())
                        .rprsvEmladr(mailBizVo.rprsvEmladr())
                        .build();
        }
}
