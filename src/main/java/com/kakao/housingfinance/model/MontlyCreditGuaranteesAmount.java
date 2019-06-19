package com.kakao.housingfinance.model;

import com.kakao.housingfinance.model.audit.DateAudit;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name="MONTHLY_CREDIT_GURANTEES_AMOUNT")
public class MontlyCreditGuaranteesAmount extends DateAudit {
    @Id
    @Column(name = "AMOUNT_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amaount_seq")
    private BigInteger id;

    @Column(name = "YEAR")
    private String year;

    @Column(name = "MONTH")
    private String month;

    @ManyToOne
    @OneToOne(targetEntity = Bank.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "BANK_ID")
    private Bank bank;

    @Column(name = "AMOUNT")
    private BigInteger amount;

    public MontlyCreditGuaranteesAmount() { }
    public MontlyCreditGuaranteesAmount(String year, String month, Bank bank, BigInteger amount){
        this.year = year;
        this.month = month;
        this.bank = bank;
        this.amount = amount;
    }

    public BigInteger getId() {
        return id;
    }
    public String getYear() {
        return year;
    }
    public String getMonth() {
        return month;
    }
    public Bank getBank() {
        return bank;
    }
    public BigInteger getAmount() {
        return amount;
    }
}
