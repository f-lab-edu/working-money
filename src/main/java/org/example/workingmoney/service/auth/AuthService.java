package org.example.workingmoney.service.auth;

import lombok.RequiredArgsConstructor;
import org.example.workingmoney.domain.entity.UserRole;
import org.example.workingmoney.repository.user.UserRepository;
import org.example.workingmoney.service.auth.exception.DuplicatedEmailException;
import org.example.workingmoney.service.auth.exception.DuplicatedNicknameException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public boolean checkEmailDuplicate(String email) {
        return !userRepository.existsByEmail(email);
    }

    @Transactional
    public boolean checkNicknameDuplicate(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    @Transactional
    public void join(String email, String nickname, String rawPassword, UserRole role) {

        userRepository.lockByEmail(email);
        userRepository.lockByNickname(nickname);

        if (userRepository.existsByEmail(email)) {
            throw new DuplicatedEmailException();
        }
        if (userRepository.existsByNickname(nickname)) {
            throw new DuplicatedNicknameException();
        }

        String hashedPassword = passwordEncoder.encode(rawPassword);
        userRepository.create(hashedPassword, nickname, email, role.name());
    }

    @Transactional
    public void updateRefreshToken(Long id, String refreshToken) {
        userRepository.updateRefreshTokenById(id, refreshToken);
    }

    @Transactional
    public void updateRefreshToken(String email, String refreshToken) {
        userRepository.updateRefreshTokenByEmail(email, refreshToken);
    }

    @Transactional
    public void deleteRefreshToken(Long id) {
        userRepository.updateRefreshTokenById(id, null);
    }

    @Transactional
    public void deleteRefreshToken(String email) {
        userRepository.updateRefreshTokenByEmail(email, null);
    }

    @Transactional
    public String findRefreshTokenById(Long id) {
        return userRepository.findRefreshTokenById(id).orElseThrow();
    }
}
