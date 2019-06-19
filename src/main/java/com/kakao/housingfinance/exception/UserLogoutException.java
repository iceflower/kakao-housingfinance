package com.kakao.housingfinance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class UserLogoutException extends RuntimeException {

    private final String user;
    private final String message;

    public UserLogoutException(String message) {
        super(message);
        this.user = null;
        this.message = message;
    }
    public UserLogoutException(String user, String message) {
        super(String.format("사용자 [%s] 의 로그아웃을 진행할 수 없습니다. : [%s])", user, message));
        this.user = user;
        this.message = message;
    }
}