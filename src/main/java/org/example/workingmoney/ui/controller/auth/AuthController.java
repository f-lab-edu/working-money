package org.example.workingmoney.ui.controller.auth;

import lombok.RequiredArgsConstructor;
import org.example.workingmoney.domain.entity.UserRole;
import org.example.workingmoney.service.auth.AuthService;
import org.example.workingmoney.ui.controller.common.Response;
import org.example.workingmoney.ui.dto.request.JoinRequestDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/join")
    public Response<Void> join(@RequestBody @Valid JoinRequestDto joinRequestDto) {
        authService.join(joinRequestDto.email(), joinRequestDto.nickname(), joinRequestDto.rawPassword(), UserRole.NORMAL_USER);
        return Response.ok(null);
    }
}
