package com.bank.model.dto.response;

import java.time.Instant;

public record ApiResponse<T>(
        boolean success,
        T data,
        String errorCode,
        String errorMessage,
        Instant timestamp
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null, null, Instant.now());
    }

    public static <T> ApiResponse<T> error(String errorCode, String errorMessage) {
        return new ApiResponse<>(false, null, errorCode, errorMessage, Instant.now());
    }
}