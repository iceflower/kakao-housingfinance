package com.kakao.housingfinance.model.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "토큰 새로고침 요청", description = "jwt 토큰 새로고침 요청 payload")
public class TokenRefreshRequest {

    @NotBlank(message = "리프레시 토큰은 공백이 아니어야 합니다.")
    @ApiModelProperty(value = "인증 토큰이 통과되기 전에 사용할 유효한 리프레시 토큰", required = true,
            allowableValues = "공백이 아닌 문자열")
    private String refreshToken;

    public TokenRefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public TokenRefreshRequest() {
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
