package com.kakao.housingfinance.model.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@ApiModel(value = "로그아웃 요청", description = "로그아웃 요청 payload")
public class LogOutRequest {

    @Valid
    @NotNull(message = "디바이스 정보를 찾을 수 없습니다.")
    @ApiModelProperty(value = "디바이스 정보", required = true, dataType = "object", allowableValues = "유효한 디바이스 정보")
    private DeviceInfo deviceInfo;

    public LogOutRequest() {
    }

    public LogOutRequest(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

}
