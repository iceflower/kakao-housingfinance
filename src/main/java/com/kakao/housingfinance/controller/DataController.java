package com.kakao.housingfinance.controller;

import com.kakao.housingfinance.model.payload.ApiResponse;
import com.kakao.housingfinance.service.FinanceDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/data")
@Api(value = "데이터 업로드 REST API")
public class DataController {
    private final FinanceDataService financeDataService;

    @Autowired
    public DataController(FinanceDataService financeDataService) {
        this.financeDataService = financeDataService;
    }
    @PostMapping(value = "/upload")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ApiOperation(value = "문제에서 제시된 csv 파일을 multipart로 업로드 받는 API")
    public ResponseEntity handleFileUpload(@ApiParam(value = "multipart 파일 업로드 매개변수") @RequestParam("fileUpload") MultipartFile fileUpload) throws Exception {
        /*
        TODO: 업로드한 파일이 없거나 1개 이상 업로드 요청이 되었을 떄 예외발생 시켜야 함.
         */

        if (fileUpload != null) financeDataService.loadFinancdDataFromCsvFile(fileUpload);



        return ResponseEntity.ok(new ApiResponse("데이터 업로드 성공", true));
    }
}
