package com.kakao.housingfinance.repository;

import com.kakao.housingfinance.model.MontlyCreditGuaranteesAmount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface MontlyCreditGuaranteesAmountRepository extends JpaRepository<MontlyCreditGuaranteesAmount, BigInteger> {
}
