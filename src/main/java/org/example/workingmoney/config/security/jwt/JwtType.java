package org.example.workingmoney.config.security.jwt;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum JwtType {
    ACCESS(15 * 60 * 1000L, "access"), // 15분
    REFRESH(7 * 24 * 60 * 60 * 1000L, "refresh");  // 7일

    // 토큰 만료 시간 - millisecond
    private final Long expirationTime;
    private final String categoryName;

    JwtType(Long expirationTime, String category) {
        this.expirationTime = expirationTime;
        this.categoryName = category;
    }

    public static Optional<JwtType> makeJwtType(String category) {
        return Arrays.stream(values())
                .filter(type -> type.categoryName.equals(category))
                .findFirst();
    }
}