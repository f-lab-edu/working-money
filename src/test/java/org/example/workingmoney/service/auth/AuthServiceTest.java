package org.example.workingmoney.service.auth;

import org.example.workingmoney.domain.entity.UserRole;
import org.example.workingmoney.repository.user.UserRepository;
import org.example.workingmoney.service.auth.exception.DuplicatedEmailException;
import org.example.workingmoney.service.auth.exception.DuplicatedNicknameException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void join_success_savesHashedPassword() {
        // given
        String email = "test@example.com";
        String nickname = "tester";
        String rawPassword = "password123!";
        String hashedPassword = "hashed_password";

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.existsByNickname(nickname)).thenReturn(false);
        when(passwordEncoder.encode(rawPassword)).thenReturn(hashedPassword);

        // when & then
        assertDoesNotThrow(() -> authService.join(email, nickname, rawPassword, UserRole.NORMAL_USER));

        verify(userRepository, times(1)).existsByEmail(email);
        verify(userRepository, times(1)).existsByNickname(nickname);
        verify(passwordEncoder, times(1)).encode(rawPassword);
        verify(userRepository, times(1)).create(hashedPassword, nickname, email, UserRole.NORMAL_USER.name());
    }

    @Test
    void join_fail_whenEmailDuplicated() {
        // given
        String email = "dup@example.com";
        String nickname = "dupNick";
        String rawPassword = "rawPassword!";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        // when & then
        assertThrows(DuplicatedEmailException.class, () -> authService.join(email, nickname, rawPassword, UserRole.NORMAL_USER));

        verify(userRepository, times(1)).existsByEmail(email);
        verify(userRepository, never()).existsByNickname(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).create(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void join_fail_whenNicknameDuplicated() {
        // given
        String email = "ok@example.com";
        String nickname = "dupNick";
        String rawPassword = "rawPassword!";

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.existsByNickname(nickname)).thenReturn(true);

        // when & then
        assertThrows(DuplicatedNicknameException.class, () -> authService.join(email, nickname, rawPassword, UserRole.NORMAL_USER));

        verify(userRepository, times(1)).existsByEmail(email);
        verify(userRepository, times(1)).existsByNickname(nickname);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).create(anyString(), anyString(), anyString(), anyString());
    }
}