package com.kakao.housingfinance.model.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "패스워드 초기화 요청", description = "패스워드 초기화 링크 요청 payload")
public class PasswordResetLinkRequest {

    @NotBlank(message = "이메일 주소가 존재하지 않습니다.")
    @ApiModelProperty(value = "사용자가 등록한 이메일", required = true, allowableValues = "공백이 아닌 문자열")
    private String email;

    public PasswordResetLinkRequest(String email) {
        this.email = email;
    }

    public PasswordResetLinkRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
