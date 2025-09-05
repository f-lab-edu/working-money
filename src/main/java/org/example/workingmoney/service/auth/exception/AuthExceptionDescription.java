package org.example.workingmoney.service.auth.exception;

import org.example.workingmoney.service.common.exception.ExceptionDescribable;

public enum AuthExceptionDescription implements ExceptionDescribable {

    DUPLICATED_EMAIL(20001, "email exists"),
    DUPLICATED_NICKNAME(20002, "nickname exists"),
    ;

    private final int code;
    private final String defaultMessage;

    AuthExceptionDescription(int code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public int code() { return code; }
    public String defaultMessage() { return defaultMessage; }
}

