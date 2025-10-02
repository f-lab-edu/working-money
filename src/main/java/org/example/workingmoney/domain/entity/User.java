package org.example.workingmoney.domain.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public class User {

    @NonNull
    private final Long id;
    @NonNull
    private final String nickname;
    @NonNull
    private final String email;
    @NonNull
    private final String password;
    @NonNull
    private final String role;
}
