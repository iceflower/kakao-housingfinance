package com.kakao.housingfinance.model.payload;

import com.kakao.housingfinance.model.BankSumCreditGuaranteesAmount;

import java.math.BigInteger;

public class BankAmountResponse {
    private String bankName;
    private BigInteger amount;


    public  BankAmountResponse(BankSumCreditGuaranteesAmount bankAmount) {
        this.bankName = bankAmount.getBankName();
        this.amount = bankAmount.getAmount();
    }
    public  BankAmountResponse(String bankName, BigInteger amount) {
        this.bankName = bankName;
        this.amount = amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankName() {
        return bankName;
    }
}
