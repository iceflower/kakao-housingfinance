package com.kakao.housingfinance.service;

import com.kakao.housingfinance.model.Bank;
import com.kakao.housingfinance.model.MontlyCreditGuaranteesAmount;
import com.kakao.housingfinance.repository.BankRepository;
import com.kakao.housingfinance.repository.MontlyCreditGuaranteesAmountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FinanceDataService {
    private final FileUtill fileUtill;
    private final BankRepository bankRepository;
    private final MontlyCreditGuaranteesAmountRepository montlyCreditGuaranteesAmountRepository;

    @Autowired
    public FinanceDataService(FileUtill fileUtill, BankRepository bankRepository, MontlyCreditGuaranteesAmountRepository montlyCreditGuaranteesAmountRepository) {
        this.fileUtill = fileUtill;
        this.bankRepository = bankRepository;
        this.montlyCreditGuaranteesAmountRepository = montlyCreditGuaranteesAmountRepository;
    }

    public void loadFinancdDataFromCsvFile(MultipartFile file) throws IOException, InterruptedException, ExecutionException {
        List<String> companyList = fileUtill.getCompanyList(file).get();
        List<String[]> financeDataList = fileUtill.getFinanceData(file).get();

        companyList.stream().forEach(company->{
            Bank newBank = new Bank(company);
            bankRepository.save(newBank);
        });

        financeDataList.stream().forEachOrdered(dataList->{
            String year = dataList[0];
            String month = dataList[1];

            int offset = 2;
            for(int i=2; i<dataList.length;i++) {
                Bank bank = bankRepository.findByName(companyList.get(i-offset));
                MontlyCreditGuaranteesAmount guaranteesAmount = new MontlyCreditGuaranteesAmount(year, month, bank, new BigInteger(dataList[i]));
                montlyCreditGuaranteesAmountRepository.save(guaranteesAmount);

            }
        });

    }

}
