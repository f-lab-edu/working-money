package org.example.workingmoney.ui.controller.auth;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.workingmoney.config.security.jwt.AuthTokenUtil;
import org.example.workingmoney.config.security.jwt.JwtType;
import org.example.workingmoney.domain.entity.UserRole;
import org.example.workingmoney.service.auth.AuthService;
import org.example.workingmoney.ui.controller.common.Response;
import org.example.workingmoney.ui.dto.request.JoinRequestDto;
import jakarta.validation.Valid;
import org.example.workingmoney.ui.dto.request.LogoutRequestDto;
import org.example.workingmoney.ui.dto.request.ReissueJwtRequestDto;
import org.example.workingmoney.ui.dto.response.AuthTokensResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthTokenUtil authTokenUtil;

    @PostMapping("/join")
    public Response<Void> join(@RequestBody @Valid JoinRequestDto joinRequestDto) {
        authService.join(joinRequestDto.email(), joinRequestDto.nickname(), joinRequestDto.rawPassword(), UserRole.NORMAL_USER);
        return Response.ok(null);
    }

    @PostMapping("/reissue")
    public Response<AuthTokensResponse> reissue(@RequestBody ReissueJwtRequestDto reissueJwtRequestDto) {
        String refreshToken = reissueJwtRequestDto.refreshToken();

        if (refreshToken == null) {

            throw new IllegalArgumentException("refresh token is null");
        }

        if (!authTokenUtil.isValid(refreshToken)) {

            throw new IllegalArgumentException("refresh token is invalid");
        }

        if (!authService.findRefreshTokenById(reissueJwtRequestDto.id())
                .equals(reissueJwtRequestDto.refreshToken())) {

            throw new IllegalArgumentException("refresh token is invalid");
        }

        if (authTokenUtil.isExpired(refreshToken)) {

            throw new IllegalArgumentException("refresh token is expired");
        }

        Optional<JwtType> category = authTokenUtil.getCategory(refreshToken);

        if (category.isEmpty() || !category.get().equals(JwtType.REFRESH)) {

            throw new IllegalArgumentException("refresh token is invalid");
        }

        // TODO: 블랙리스트 기반 확인 로직 추가

        String username = authTokenUtil.getUsername(refreshToken);
        String role = authTokenUtil.getRole(refreshToken);

        String newAccess = authTokenUtil.createJwt(JwtType.ACCESS, username, role);
        String newRefresh = authTokenUtil.createJwt(JwtType.REFRESH, username, role);

        return Response.ok(new AuthTokensResponse(newAccess, newRefresh));
    }

    @PostMapping("/logout")
    public Response<Void> logout(@RequestBody LogoutRequestDto logoutRequestDto) {

        authService.deleteRefreshToken(logoutRequestDto.id());

        // TODO: 블랙리스트 기반 토큰만료 로직 추가

        return Response.ok(null);
    }
}
