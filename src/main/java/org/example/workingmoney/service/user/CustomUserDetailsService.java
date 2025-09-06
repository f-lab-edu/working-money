package org.example.workingmoney.service.user;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.workingmoney.repository.user.UserEntity;
import org.example.workingmoney.repository.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserEntity> userEntity = userRepository.findByEmail(username);

        if (userEntity.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        return new CustomUserDetails(userEntity.get().toDomain());
    }
}
