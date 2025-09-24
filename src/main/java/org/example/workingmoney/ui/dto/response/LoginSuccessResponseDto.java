package org.example.workingmoney.ui.dto.response;

public record LoginSuccessResponseDto(
        UserInfoResponseDto userInfo,
        AuthTokensResponse authTokens) {

}
