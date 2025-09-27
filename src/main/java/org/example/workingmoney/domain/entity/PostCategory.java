package org.example.workingmoney.domain.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum PostCategory {
    KOREAN_STOCKS("korean_stocks"),
    AMERICAN_STOCKS("american_stocks"),
    CRYPTO_CURRENCY("crypto_currency");

    private final String code;

    PostCategory(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PostCategory from(String value) {
        return Arrays.stream(values())
                .filter(c -> c.code.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid category: " + value));
    }
}


