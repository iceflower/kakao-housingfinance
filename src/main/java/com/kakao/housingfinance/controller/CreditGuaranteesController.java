package com.kakao.housingfinance.controller;

import com.kakao.housingfinance.exception.BankNotFoundException;
import com.kakao.housingfinance.model.BankSumCreditGuaranteesAmount;
import com.kakao.housingfinance.model.payload.ApiResponse;
import com.kakao.housingfinance.model.payload.SupportAmountResponse;
import com.kakao.housingfinance.model.payload.TotalAmountResponse;
import com.kakao.housingfinance.service.CreditGuaranteesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/api/creditGuarantee")
@Api(value = "주택보증 지원내역관련 API")
public class CreditGuaranteesController {
    private final CreditGuaranteesService creditGuaranteesService;


    @Autowired
    public CreditGuaranteesController(CreditGuaranteesService creditGuaranteesService) {
        this.creditGuaranteesService = creditGuaranteesService;
    }

    @GetMapping("/totalAmountByYear")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @ApiOperation(value = "전체 금융기관의 전체 보증금액 내역 조회")
    public ResponseEntity totalAmountByYear() throws BankNotFoundException {
        List<TotalAmountResponse> result = creditGuaranteesService.sumGuaranteesAmount();


        if(result.size() == 0) throw new BankNotFoundException("데이터가 존재하지 않습니다.");

        return ResponseEntity.ok(new ApiResponse(result, true));
    }

    @GetMapping("/maxAmountOfBank")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @ApiOperation(value = "전체 금융기관의 보증내역 중, 가장 많은 지원을 한 금융기관과 그 연도를 조회")
    public ResponseEntity maxAmountOfBank(){
        BankSumCreditGuaranteesAmount result = creditGuaranteesService.findMaxAmountByBank();
        return ResponseEntity.ok(new ApiResponse(result, true));
    }

    @GetMapping("/supportAmountOfBank")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @ApiOperation(value = "특정 금융기관의 역대 최저/최대 보증금액 조회")
    public ResponseEntity supportAmountOfBank(@ApiParam(value = "은행 이름") @RequestParam(name = "bankName", required = true) String bankName) throws UnsupportedEncodingException {

        SupportAmountResponse result = creditGuaranteesService.getSupportAmountByBank(bankName);
        return ResponseEntity.ok(new ApiResponse(result, true));
    }


}
