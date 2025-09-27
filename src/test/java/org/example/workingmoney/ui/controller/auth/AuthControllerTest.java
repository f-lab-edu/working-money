package org.example.workingmoney.ui.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.workingmoney.config.security.jwt.AuthTokenUtil;
import org.example.workingmoney.domain.entity.UserRole;
import org.example.workingmoney.service.auth.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private AuthTokenUtil authTokenUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 회원가입_성공_테스트() throws Exception {
        // given
        var json = """
            {
              "email": "test@example.com",
              "nickname": "tester1",
              "rawPassword": "Password1234"
            }
            """;

        // when & then
        mockMvc.perform(post("/api/v1/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.message").value("ok"));

        verify(authService, times(1))
                .join("test@example.com", "tester1", "Password1234", UserRole.NORMAL_USER);
    }

    @Test
    void 회원가입_이메일_유효성_검사_실패_테스트() throws Exception {
        // given
        var json = """
            {
              "email": "not-an-email",
              "nickname": "tester1",
              "rawPassword": "Password1234"
            }
            """;

        // when & then
        mockMvc.perform(post("/api/v1/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(authService, never()).join(anyString(), anyString(), anyString(), eq(UserRole.NORMAL_USER));
    }

    @Test
    void 회원가입_닉네임_유효성_검사_실패_테스트() throws Exception {
        // given
        // 닉네임은 최소 2자, 최대 10자, 영문/숫자/한글만 허용
        var json = """
            {
              "email": "test@example.com",
              "nickname": "a",
              "rawPassword": "Password1234"
            }
            """;

        // when & then
        mockMvc.perform(post("/api/v1/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(authService, never()).join(anyString(), anyString(), anyString(), eq(UserRole.NORMAL_USER));
    }

    @Test
    void 회원가입_패스워드_유효성_검사_실패_테스트() throws Exception {
        // given
        // 패스워드는 영문/숫자 포함 12자 이상, 특수문자 불가
        var json = """
            {
              "email": "test@example.com",
              "nickname": "tester1",
              "rawPassword": "short1"
            }
            """;

        // when & then
        mockMvc.perform(post("/api/v1/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(authService, never()).join(anyString(), anyString(), anyString(), eq(UserRole.NORMAL_USER));
    }
}