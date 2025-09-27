package org.example.workingmoney.service.auth.exception;

import org.example.workingmoney.service.common.exception.CustomException;

public class DuplicatedNicknameException extends CustomException {

    public DuplicatedNicknameException() {
        super(AuthExceptionDescription.DUPLICATED_NICKNAME);
    }

    public DuplicatedNicknameException(String message) {
        super(AuthExceptionDescription.DUPLICATED_NICKNAME, message);
    }
}
