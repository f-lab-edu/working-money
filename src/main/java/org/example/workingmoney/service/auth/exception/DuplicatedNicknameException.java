package org.example.workingmoney.service.auth.exception;

import org.example.workingmoney.service.common.exception.CustomException;

public class DuplicatedNicknameException extends CustomException {

    public DuplicatedNicknameException() {
        super(AuthExceptionDescription.Duplicated_NICKNAME);
    }

    public DuplicatedNicknameException(String message) {
        super(AuthExceptionDescription.Duplicated_NICKNAME, message);
    }
}
