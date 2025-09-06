package org.example.workingmoney.ui.controller.common;

import org.example.workingmoney.service.common.exception.CustomException;
import org.springframework.http.HttpStatusCode;

public record Response<T>(int statusCode, String message, T value) {

    public static <T> Response<T> ok(T value) {
        return new Response<>(0, "ok", value);
    }

    public static <T> Response<T> error(CustomException exception) {
        return new Response<>(exception.getCode(), exception.getMessage(), null);
    }

    public static <T> Response<T> error(Exception exception, int statusCode) {
        return new Response<>(statusCode, exception.getMessage(), null);
    }
}
