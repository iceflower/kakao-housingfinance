package com.kakao.housingfinance.util;

import java.util.UUID;

public class Util {

//    public static Boolean isTrue(Boolean value) {
//        return value;
//    }
    public static String generateRandomUuid() {
        return UUID.randomUUID().toString();
    }
}
