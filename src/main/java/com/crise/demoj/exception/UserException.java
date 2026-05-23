package com.crise.demoj.exception;

import com.crise.demoj.dto.api.IErrorCode;

public class UserException extends RuntimeException {
    private IErrorCode errorCode;

    public UserException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public UserException(String message) {
        super(message);
    }

    public UserException(IErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }
}
