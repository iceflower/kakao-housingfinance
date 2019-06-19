package com.kakao.housingfinance.model;

import com.kakao.housingfinance.model.audit.DateAudit;

import javax.persistence.*;

@Entity
@Table(name = "BANK")
public class Bank extends DateAudit {

    @Id
    @Column(name = "BANK_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_seq")
    private long id;

    @Column(name = "NAME")
    private String name;

    public Bank() {

    }
    public Bank(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
