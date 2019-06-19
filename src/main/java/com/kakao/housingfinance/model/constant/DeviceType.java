package com.kakao.housingfinance.model.constant;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DeviceType {
    DEVICE_TYPE_ANDROID("DEVICE_TYPE_ANDROID"), //안드로이드
    DEVICE_TYPE_IOS("DEVICE_TYPE_IOS"), //아이폰
    DEVICE_TYPE_DESKTOP_WEB("DEVICE_TYPE_DESKTOP_WEB"), //데스크톱 웹
    DEVICE_TYPE_MOBILE_WEB("DEVICE_TYPE_MOBILE_WEB"); //모바일 웹

    @JsonValue
    private final String code;

    DeviceType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
