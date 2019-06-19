package com.kakao.housingfinance.model;

import java.math.BigInteger;

public class SumCreditGuaranteesAmount {
    private String year;
    private BigInteger sum;

    public SumCreditGuaranteesAmount(String year, BigInteger sum) {
        this.year = year;
        this.sum = sum;
    }
    public void setYear(String year) {
        this.year = year;
    }

    public String getYear() {
        return year;
    }

    public void setSum(BigInteger sum) {
        this.sum = sum;
    }

    public BigInteger getSum() {
        return sum;
    }
}
