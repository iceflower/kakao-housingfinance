package com.kakao.housingfinance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class MailSendException extends RuntimeException {

    private final String recipientAddress;
    private final String message;

    public MailSendException(String recipientAddress, String message) {
        super(String.format("[%s] 로 메일 전송을 시도하였으나, 실패하였습니다. [%s]", recipientAddress, message));
        this.recipientAddress = recipientAddress;
        this.message = message;
    }
}