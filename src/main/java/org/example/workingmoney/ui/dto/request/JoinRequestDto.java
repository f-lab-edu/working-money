package org.example.workingmoney.ui.dto.request;

import jakarta.validation.constraints.NotNull;
import org.example.workingmoney.common.validation.ValidEmail;
import org.example.workingmoney.common.validation.ValidNickname;
import org.example.workingmoney.common.validation.ValidPassword;

public record JoinRequestDto(
        @ValidEmail
        @NotNull
        String email,

        @ValidNickname
        @NotNull
        String nickname,

        @ValidPassword
        @NotNull
        String rawPassword
) {

}