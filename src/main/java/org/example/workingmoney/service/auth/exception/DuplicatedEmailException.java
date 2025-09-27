package org.example.workingmoney.service.auth.exception;

import org.example.workingmoney.service.common.exception.CustomException;

public class DuplicatedEmailException extends CustomException {

    public DuplicatedEmailException() {
        super(AuthExceptionDescription.DUPLICATED_EMAIL);
    }

    public DuplicatedEmailException(String message) {
        super(AuthExceptionDescription.DUPLICATED_EMAIL, message);
    }
}
