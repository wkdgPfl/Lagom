package com.lagom.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

// 우리 서비스에서 발생하는 예외를 담는 클래스
// RuntimeException을 상속받아서 throw 가능
@Getter
public class CustomException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public CustomException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }
}