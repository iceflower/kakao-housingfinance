package com.kakao.housingfinance.model.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "비밀번호 수정 요청", description = "이메일 수정 요청 payload")
public class UpdatePasswordRequest {

    @NotBlank(message = "기존 비밀번호는 공백이 아니어야 합니다.")
    @ApiModelProperty(value = "유효한 비밀번호 문자열", required = true, allowableValues = "공백이 아닌 문자열")
    private String oldPassword;

    @NotBlank(message = "새로운 비밀번호는 공백이 아니어야 합니다.")
    @ApiModelProperty(value = "유효한 비밀번호 문자열", required = true, allowableValues = "공백이 아닌 문자열")
    private String newPassword;

    public UpdatePasswordRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public UpdatePasswordRequest() {
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
