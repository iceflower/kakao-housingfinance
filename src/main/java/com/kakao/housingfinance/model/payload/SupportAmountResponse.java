package com.kakao.housingfinance.model.payload;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

public class SupportAmountResponse {
    private String bankName;
    private List<Map<String, Object>> supportAmount;

    public SupportAmountResponse(String bankName, List<Map<String, Object>> supportAmount) {
        this.bankName = bankName;
        this.supportAmount = supportAmount;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setSupportAmount(List<Map<String, Object>> supportAmount) {
        this.supportAmount = supportAmount;
    }

    public List<Map<String, Object>> getSupportAmount() {
        return supportAmount;
    }
}
