package com.kakao.housingfinance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class PasswordResetLinkException extends RuntimeException {

    private final String user;
    private final String message;

    public PasswordResetLinkException(String user, String message) {
        super(String.format("[%s] 사용자에게 비밀번호 초기화 링크를 전송하는 데 실패하였습니다. : '%s'", user, message));
        this.user = user;
        this.message = message;
    }
}