package com.kakao.housingfinance.model.payload;

import com.kakao.housingfinance.validation.annotation.NullOrNotBlank;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel(value = "등록 요청", description = "등록 요청용 payload")
public class RegistrationRequest {

    @NullOrNotBlank(message = "사용자 계정명은 공백이 아니어야 합니다.")
    @ApiModelProperty(value = "유효한 사용자 계정명", allowableValues = "공백이 아닌 문자열")
    private String username;

    @NullOrNotBlank(message = "이메일 주소는 공백이 아니어야 합니다.")
    @ApiModelProperty(value = "유효한 메일계정", required = true, allowableValues = "공백이 아닌 문자열")
    private String email;

    @NotNull(message = "비밀번호는 공백이 아니어야 합니다.")
    @ApiModelProperty(value = "유효한 비밀번호", required = true, allowableValues = "공백이 아닌 문자열")
    private String password;

    @NotNull(message = "관리자 계정인지 아닌지 명확하게 설정하여야 합니다.")
    @ApiModelProperty(value = "관리자 계정 여부 플래그", required = true,
            dataType = "boolean", allowableValues = "true, false")
    private Boolean registerAsAdmin;

    public RegistrationRequest(String username, String email,
                               String password, Boolean registerAsAdmin) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.registerAsAdmin = registerAsAdmin;
    }

    public RegistrationRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getRegisterAsAdmin() {
        return registerAsAdmin;
    }

    public void setRegisterAsAdmin(Boolean registerAsAdmin) {
        this.registerAsAdmin = registerAsAdmin;
    }
}
