package org.example.workingmoney.ui.controller.common;

import org.example.workingmoney.service.common.exception.CustomException;
import org.springframework.http.HttpStatusCode;

public record Response<T>(Integer code, String message, T value) {

    public static <T> Response<T> ok(T value) {
        return new Response<>(1, "ok", value);
    }

    public static <T> Response<T> error(CustomException exception) {
        return new Response<>(exception.getCode(), exception.getMessage(), null);
    }

    public static <T> Response<T> error(Exception exception) {
        return new Response<>(null, exception.getMessage(), null);
    }
}
