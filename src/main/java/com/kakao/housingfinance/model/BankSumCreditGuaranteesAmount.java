package com.kakao.housingfinance.model;

import java.math.BigInteger;

public class BankSumCreditGuaranteesAmount {
    private String year;
    private String bankName;
    private BigInteger amount;


    public BankSumCreditGuaranteesAmount(String year, BigInteger amount) {
        this.year = year;
        this.amount = amount;
    }
    public BankSumCreditGuaranteesAmount(String year, String bankName, BigInteger amount) {
        this.year = year;
        this.bankName = bankName;
        this.amount = amount;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getYear() {
        return year;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public BigInteger getAmount() {
        return amount;
    }
}
