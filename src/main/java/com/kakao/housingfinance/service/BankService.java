package com.kakao.housingfinance.service;

import com.kakao.housingfinance.exception.BankNotFoundException;
import com.kakao.housingfinance.model.Bank;
import com.kakao.housingfinance.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankService {

    private final BankRepository bankRepository;

    @Autowired
    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    /**
    *  모든 금융기관 정보를 조회함
     */
    public List<Bank> allBankList() {
        return Optional.ofNullable(bankRepository.findAll()).orElseThrow(()->new BankNotFoundException("존재하지 않는 금융기관입니다."));
    }

    /**
     *  이름으로 금융기관 조회
     */
    public Bank findByName(String name) {
        return Optional.ofNullable(bankRepository.findByName(name)).orElseThrow(()->new BankNotFoundException("존재하지 않는 금융기관입니다."));
    }


}
