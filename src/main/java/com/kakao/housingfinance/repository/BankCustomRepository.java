package com.kakao.housingfinance.repository;

import com.kakao.housingfinance.exception.BankNotFoundException;
import com.kakao.housingfinance.model.*;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class BankCustomRepository extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;
    private final BankRepository bankRepository;

    @Autowired
    public BankCustomRepository(JPAQueryFactory queryFactory, BankRepository bankRepository) {
        super(BankCustomRepository.class);
        this.queryFactory = queryFactory;
        this.bankRepository = bankRepository;
    }


    public List<SumCreditGuaranteesAmount> sumGuaranteesAmountOfYear() {

        QMontlyCreditGuaranteesAmount gAmount = QMontlyCreditGuaranteesAmount.montlyCreditGuaranteesAmount;


        return queryFactory
                .select(Projections.constructor(SumCreditGuaranteesAmount.class, gAmount.year, gAmount.amount.sum()))
                .from(gAmount)
                .groupBy(gAmount.year)
                .fetch();
    }

    public List<BankSumCreditGuaranteesAmount> sumBankGuaranteesAmountOfYear() {

        QMontlyCreditGuaranteesAmount gAmount = QMontlyCreditGuaranteesAmount.montlyCreditGuaranteesAmount;


        return queryFactory
                .select(Projections.constructor(BankSumCreditGuaranteesAmount.class, gAmount.year, gAmount.bank.name.as("bankName"), gAmount.amount.sum().as("amount")))
                .from(gAmount)
                .groupBy(gAmount.bank.name, gAmount.year)
                .fetch();
    }

    public BankSumCreditGuaranteesAmount findMaxAmountByBank() {
        QMontlyCreditGuaranteesAmount gAmount = QMontlyCreditGuaranteesAmount.montlyCreditGuaranteesAmount;

        return queryFactory
                .select(Projections.constructor(BankSumCreditGuaranteesAmount.class, gAmount.year, gAmount.bank.name.as("bankName"), gAmount.amount.sum().as("amount")))
                .from(gAmount)
                .groupBy(gAmount.bank.name, gAmount.year)
                .orderBy(gAmount.amount.sum().desc())
                .limit(1)
                .fetchOne();
    }


    public List<BankSumCreditGuaranteesAmount> getSupportAmountByBank(String bankName) throws BankNotFoundException{

        Optional.ofNullable(bankRepository.findByName(bankName)).orElseThrow(()->new BankNotFoundException("존재하지 않는 은행입니다."));



        QMontlyCreditGuaranteesAmount gAmount = QMontlyCreditGuaranteesAmount.montlyCreditGuaranteesAmount;
        List<BankSumCreditGuaranteesAmount> result = new ArrayList<>();


        BankSumCreditGuaranteesAmount minAmount =
                queryFactory
                    .select(Projections.constructor(BankSumCreditGuaranteesAmount.class, gAmount.year, gAmount.amount.sum().as("amount")))
                    .from(gAmount)
                    .groupBy(gAmount.bank, gAmount.year)
                    .where(gAmount.bank.name.eq(bankName))
                    .orderBy(gAmount.amount.sum().asc())
                    .limit(1)
                    .fetchOne();


        BankSumCreditGuaranteesAmount maxAmount =
                queryFactory
                        .select(Projections.constructor(BankSumCreditGuaranteesAmount.class, gAmount.year, gAmount.amount.sum().as("amount")))
                        .from(gAmount)
                        .groupBy(gAmount.bank, gAmount.year)
                        .where(gAmount.bank.name.eq(bankName))
                        .orderBy(gAmount.amount.sum().desc())
                        .limit(1)
                        .fetchOne();

        result.add(minAmount);
        result.add(maxAmount);

        return  result;

    }

}
