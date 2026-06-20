package com.crise.demoj.exception;

import com.crise.demoj.dto.api.IErrorCode;
import com.crise.demoj.dto.api.ResultCode;

public class UserException extends RuntimeException {
    private final IErrorCode errorCode;

    public UserException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

/**
 * Constructs a new UserException with the specified detail message.
 * The message is saved for later retrieval by the getMessage() method.
 *
 * @param message The detail message which is saved for later retrieval by the getMessage() method.
 */
    public UserException(String message) {
        super(message);
        this.errorCode = ResultCode.USER_FAILED;
    }

    public UserException(IErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }
}
