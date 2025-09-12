package org.example.workingmoney.config.security.jwt;

import lombok.Getter;

@Getter
enum ClaimNameConstants {
    USERNAME("username"),
    ROLE("role"),
    CATEGORY("category");

    private final String value;

    ClaimNameConstants(String value) {
        this.value = value;
    }
}
