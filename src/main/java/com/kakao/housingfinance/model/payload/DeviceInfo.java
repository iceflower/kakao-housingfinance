package com.kakao.housingfinance.model.payload;

import com.kakao.housingfinance.model.constant.DeviceType;
import com.kakao.housingfinance.validation.annotation.NullOrNotBlank;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class DeviceInfo {

    @NotBlank(message = "디바이스 ID는 공백이 아니어야 합니다.")
    @ApiModelProperty(value = "디바이스ID ", required = true, dataType = "string", allowableValues = "공백이 아닌 문자열")
    private String deviceId;

    @NotNull(message = "디바이스 타입은 공백이 아니어야 합니다.")
    @ApiModelProperty(value = "Device type Android/iOS", required = true, dataType = "string", allowableValues =
            "DEVICE_TYPE_ANDROID, DEVICE_TYPE_IOS, DEVICE_TYPE_DESKTOP_WEB, DEVICE_TYPE_MOBILE_WEB")
    private DeviceType deviceType;

    @NullOrNotBlank(message = "디바이스 노티피케이션 토큰은 null 은 허용하지만 공백은 허용하지 않습니다.")
    @ApiModelProperty(value = "디바이스 노티피케이션 ID", dataType = "string", allowableValues = "공백이 아닌 문자열, null")
    private String notificationToken;

    public DeviceInfo() {
    }

    public DeviceInfo(String deviceId, DeviceType deviceType, String notificationToken) {
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.notificationToken = notificationToken;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getNotificationToken() {
        return notificationToken;
    }

    public void setNotificationToken(String notificationToken) {
        this.notificationToken = notificationToken;
    }
}
