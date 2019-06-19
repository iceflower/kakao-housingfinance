package com.kakao.housingfinance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class TokenRefreshException extends RuntimeException {

    private final String token;
    private final String message;

    public TokenRefreshException(String token, String message) {
        super(String.format("[%s] 을 재발급하지 못했습니다. : [%s])", token, message));
        this.token = token;
        this.message = message;
    }
}