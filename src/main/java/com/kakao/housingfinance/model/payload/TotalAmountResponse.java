package com.kakao.housingfinance.model.payload;

import java.math.BigInteger;
import java.util.List;

public class TotalAmountResponse {
    private String year;
    private BigInteger amount;
    private List<BankAmountResponse> detailList;

    public TotalAmountResponse(String year, BigInteger amount, List<BankAmountResponse> detailList) {
        this.year = year;
        this.amount = amount;
        this.detailList = detailList;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getYear() {
        return year;
    }

    public void setDetailList(List<BankAmountResponse> detailList) {
        this.detailList = detailList;
    }

    public List<BankAmountResponse> getDetailList() {
        return detailList;
    }
}
