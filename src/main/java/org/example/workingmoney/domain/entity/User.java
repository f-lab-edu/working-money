package org.example.workingmoney.domain.entity;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public class User {

    @Nonnull
    private final Long id;
    @Nonnull
    private final String nickname;
    @Nonnull
    private final String email;
    @Nonnull
    private final String password;
    @Nonnull
    private final String role;
}
