package org.example.workingmoney.repository.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.workingmoney.domain.entity.User;
import org.example.workingmoney.repository.common.TimeBaseEntity;

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserEntity extends TimeBaseEntity {

    private Long id;
    private final String passwordHash;
    private final String nickname;
    private final String email;
    private final String role;

    public User toDomain() {
        return new User(id, nickname, email, passwordHash, role);
    }
}