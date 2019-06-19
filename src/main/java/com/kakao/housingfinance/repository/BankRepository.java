package com.kakao.housingfinance.repository;

import com.kakao.housingfinance.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {

    Bank findByName(String name);
}
