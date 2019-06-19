package com.kakao.housingfinance.controller;

import com.kakao.housingfinance.annotation.CurrentUser;
import com.kakao.housingfinance.exception.UserLogoutException;
import com.kakao.housingfinance.model.CustomUserDetails;
import com.kakao.housingfinance.model.payload.ApiResponse;
import com.kakao.housingfinance.model.payload.LogOutRequest;
import com.kakao.housingfinance.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/user")
@Api(value = "계정 관련 REST API")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @ApiOperation(value = "로그인된 디바이스 정보를 토큰과 함께 지워주는 API")
    public ResponseEntity logoutUser(@CurrentUser CustomUserDetails customUserDetails,
                                     @ApiParam(value = "로그인 요청 payload") @Valid @RequestBody LogOutRequest logOutRequest) {
        userService.logoutUser(customUserDetails, logOutRequest);
        return ResponseEntity.ok(new ApiResponse("Log out successful", true));
    }
}
