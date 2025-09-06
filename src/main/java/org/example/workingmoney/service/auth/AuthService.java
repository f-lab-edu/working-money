package org.example.workingmoney.service.auth;

import lombok.RequiredArgsConstructor;
import org.example.workingmoney.domain.entity.UserRole;
import org.example.workingmoney.repository.user.UserRepository;
import org.example.workingmoney.service.auth.exception.DuplicatedEmailException;
import org.example.workingmoney.service.auth.exception.DuplicatedNicknameException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean checkEmailDuplicate(String email) {
        return !userRepository.existsByEmail(email);
    }

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

        String hashedPassword = bCryptPasswordEncoder.encode(rawPassword);
        userRepository.create(hashedPassword, nickname, email, role.name());
    }
}
