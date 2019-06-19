package com.kakao.housingfinance.model.payload;

import com.kakao.housingfinance.validation.annotation.NullOrNotBlank;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@ApiModel(value = "로그인 요청", description = "로그인 요청용 payload")
public class LoginRequest {

    @NullOrNotBlank(message = "로그인 사용자 계정명은 null은 허용하지만, 공백이 아니어야 합니다.")
    @ApiModelProperty(value = "등록된 사용자 계정명", allowableValues = "공백이 아닌 문자열", allowEmptyValue = false)
    private String username;

    @NullOrNotBlank(message = "로그인 사용자 이메일은 null은 허용하지만, 공백이 아니어야 합니다.")
    @ApiModelProperty(value = "사용자가 등록한 이메일", required = true, allowableValues = "공백이 아닌 문자열")
    private String email;

    @NotNull(message = "디바이스 정보가 존재하지 않습니다.")
    @ApiModelProperty(value = "유효한 비밀번호", required = true, allowableValues = "공백이 아닌 문자열")
    private String password;

    @Valid
    @NotNull(message = "디바이스 정보가 존재하지 않습니다.")
    @ApiModelProperty(value = "디바이스 정보", required = true, dataType = "객체", allowableValues = "유효한 디바이스 정보 객체")
    private DeviceInfo deviceInfo;

    public LoginRequest(String username, String email, String password, DeviceInfo deviceInfo) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.deviceInfo = deviceInfo;
    }

    public LoginRequest() {
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

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}
