package org.example.workingmoney.service.common.exception;

public class InvalidFormatException extends CustomException {

    public InvalidFormatException() {
        super(CommonExceptionDescription.INVALID_FORMAT);
    }

    public InvalidFormatException(String message) {
        super(CommonExceptionDescription.INVALID_FORMAT, message);
    }
}
