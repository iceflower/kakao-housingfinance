package com.kakao.housingfinance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class UpdatePasswordException extends RuntimeException {

    private final String user;
    private final String message;

    public UpdatePasswordException(String user, String message) {
        super(String.format("[%s] 사용자의 비밀번호를 수정하지 못했습니다 : [%s])", user, message));
        this.user = user;
        this.message = message;
    }
}