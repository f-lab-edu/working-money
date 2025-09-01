package org.example.workingmoney.service.common.exception;

public class UnknownException extends CustomException {

    public UnknownException() {
        super(CommonExceptionDescription.UNKNOWN);
    }

    public UnknownException(String message) {
        super(CommonExceptionDescription.UNKNOWN, message);
    }
}
