package com.kakao.housingfinance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InvalidTokenRequestException extends RuntimeException {

    private final String tokenType;
    private final String token;
    private final String message;

    public InvalidTokenRequestException(String tokenType, String token, String message) {
        super(String.format("%s  [%s] 는 유효하지 않습니다. %s", tokenType, token, message));
        this.tokenType = tokenType;
        this.token = token;
        this.message = message;
    }
}
