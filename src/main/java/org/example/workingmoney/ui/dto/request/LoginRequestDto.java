package org.example.workingmoney.ui.dto.request;

import jakarta.validation.constraints.NotNull;

public record LoginRequestDto(
        @NotNull
        String id,

        @NotNull
        String password
) {

}


