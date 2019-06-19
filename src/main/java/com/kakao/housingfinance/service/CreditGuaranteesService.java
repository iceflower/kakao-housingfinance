package com.kakao.housingfinance.service;

import com.kakao.housingfinance.model.BankSumCreditGuaranteesAmount;
import com.kakao.housingfinance.model.SumCreditGuaranteesAmount;
import com.kakao.housingfinance.model.payload.BankAmountResponse;
import com.kakao.housingfinance.model.payload.SupportAmountResponse;
import com.kakao.housingfinance.model.payload.TotalAmountResponse;
import com.kakao.housingfinance.repository.BankCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CreditGuaranteesService {

    private final BankCustomRepository bankCustomRepository;

    @Autowired
    public CreditGuaranteesService(BankCustomRepository bankCustomRepository) {
        this.bankCustomRepository = bankCustomRepository;

    }

    /**
     * 모든 금융기관의 보증금액을 연도별 총 합계 및 연도별 금융기관 별 합계 데이터 제공
     * @return 보증금액 목록
     */
    public  List<TotalAmountResponse> sumGuaranteesAmount() {
        List<TotalAmountResponse> result = new ArrayList<>();

        List<SumCreditGuaranteesAmount> resultTotalAmountOfYear = bankCustomRepository.sumGuaranteesAmountOfYear();
        List<BankSumCreditGuaranteesAmount> resultBankAmountOfYear = bankCustomRepository.sumBankGuaranteesAmountOfYear();

        resultTotalAmountOfYear.stream().forEach(totalAmount->{
            List<BankAmountResponse> bankAmountList = new ArrayList<>();

            resultBankAmountOfYear.stream()
                    .filter(e->e.getYear().equals(totalAmount.getYear()))
                    .forEach(BankSum->bankAmountList.add(new BankAmountResponse(BankSum)));

            result.add(new TotalAmountResponse(totalAmount.getYear(), totalAmount.getSum(), bankAmountList));

        });

        return result;
    }

    /**
     * 가장 보증금액이 많았던 금융기관과 보증금액 합계, 연도를 조회함
     * @return 금융기관, 보증금액, 연도 데이터
     */
    public BankSumCreditGuaranteesAmount findMaxAmountByBank() {
        return bankCustomRepository.findMaxAmountByBank();
    }

    /**
     * 금융기관 이름으로 금융기관별 최고/최저 보증금액을 조회
     */
    public SupportAmountResponse getSupportAmountByBank(String bankName) {
        List<BankSumCreditGuaranteesAmount> supportAmount = bankCustomRepository.getSupportAmountByBank(bankName);

        List<Map<String, Object>> supportAmountMap = new ArrayList<>();

        supportAmount.stream().forEach(supportAmountElement->{
            Map<String, Object> element = new HashMap<>();

            element.put("year", supportAmountElement.getYear());
            element.put("amount", supportAmountElement.getAmount());
            supportAmountMap.add(element);
        });

        return new SupportAmountResponse(bankName, supportAmountMap);
    }

}
