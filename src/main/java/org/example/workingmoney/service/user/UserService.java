package org.example.workingmoney.service.user;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.workingmoney.domain.entity.User;
import org.example.workingmoney.repository.user.UserEntity;
import org.example.workingmoney.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findUserByEmail(String email) {
        try {
            return userRepository.findByEmail(email).map(UserEntity::toDomain);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
