package org.example.workingmoney.ui.dto.request;

import org.example.workingmoney.common.validation.ValidEmail;
import org.example.workingmoney.common.validation.ValidNickname;
import org.example.workingmoney.common.validation.ValidPassword;

public record JoinRequestDto(
        @ValidEmail String email,
        @ValidNickname String nickname,
        @ValidPassword String rawPassword
) {

}