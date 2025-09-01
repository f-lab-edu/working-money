package org.example.workingmoney.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
public class User {

    private final Long id;
    private final String nickname;
    private final String email;
}
