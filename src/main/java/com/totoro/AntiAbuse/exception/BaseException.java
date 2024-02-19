package com.totoro.AntiAbuse.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String code = "500";
    private String message;

    public BaseException() {
        this.message = "Not yet implemented";
    }

    public BaseException(String message) {
        this.message = message;
    }
    public BaseException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseException(Exception e) {
        super(e);
    }
}

