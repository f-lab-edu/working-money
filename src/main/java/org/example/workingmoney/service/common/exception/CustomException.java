package org.example.workingmoney.service.common.exception;

import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException {

    protected ExceptionDescribable exceptionDescription;

    public CustomException(ExceptionDescribable exceptionDescription) {

        super(exceptionDescription.defaultMessage());
        this.exceptionDescription = exceptionDescription;
    }

    public CustomException(ExceptionDescribable exceptionType, String message) {

        super(message);
        this.exceptionDescription = exceptionType;
    }

    public int getCode() {
        return exceptionDescription.code();
    }

    // TODO: 테스트 필요
    public String getMessage() {
        return super.getMessage().isEmpty() ? exceptionDescription.defaultMessage() : super.getMessage();
    }

    public ExceptionDescribable getDescription() {
        return exceptionDescription;
    }
}
