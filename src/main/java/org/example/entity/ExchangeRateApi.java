package org.example.entity;

import lombok.Data;

@Data
public class ExchangeRateApi {
    private Integer Code;
    private String Ccy;
    private String CcyNm_RU;
    private String CcyNm_UZ;
    private String CcyNm_EN;
    private String Nominal;
    private double Rate;
    private double Diff;
    private String Date;
}
