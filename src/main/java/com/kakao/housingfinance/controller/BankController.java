package com.kakao.housingfinance.controller;

import com.kakao.housingfinance.exception.BankNotFoundException;
import com.kakao.housingfinance.model.Bank;
import com.kakao.housingfinance.model.payload.ApiResponse;
import com.kakao.housingfinance.service.BankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bank")
@Api(value = "금융기관 조회 API")
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping("/list/all")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @ApiOperation(value = "전체 금융기관 조회")
    public ResponseEntity bankList (){
        List<Bank> result = bankService.allBankList();

        if(result.size() == 0) throw new BankNotFoundException("금융기관 데이터가 존재하지 않습니다.");

        return ResponseEntity.ok(new ApiResponse(result,true));
    }

    @GetMapping("/info")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @ApiOperation(value = "일부 금융기관 조회")
    public ResponseEntity findByBank (@ApiParam(value = "은행 이름") @RequestParam(name = "bankName", required = true) String bankName){

        return ResponseEntity.ok(new ApiResponse(bankService.findByName(bankName),true));
    }
}
