package com.momo_homework.data_pipeline.utils.error.exception;

import com.momo_homework.data_pipeline.utils.error.dtos.ApiError;
import com.momo_homework.data_pipeline.utils.error.dtos.ErrorMessage;

import java.util.Map;

public class BadRequestException extends RuntimeException {
    private ApiError apiError;

    public ApiError getApiError() {
        return apiError;
    }

    public BadRequestException(String errorMessage, Object... args) {
        super(String.format(errorMessage, args));
    }

    public BadRequestException(ErrorMessage errorMessage) {
        apiError = ApiError.builder()
                .errorId(errorMessage.getErrorId())
                .errorMessage(errorMessage.getErrorMessage())
                .status(errorMessage.getStatus())
                .data(null)
                .build();
    }

    public BadRequestException(ErrorMessage errorMessage, Object data) {
        super(errorMessage.getErrorId().concat(" - ").concat(errorMessage.getErrorMessage()));
        apiError = ApiError.builder()
                .errorId(errorMessage.getErrorId())
                .status(errorMessage.getStatus())
                .errorMessage(errorMessage.getErrorMessage())
                .data(data)
                .build();
    }

    public BadRequestException(ErrorMessage errorMessage, String message, Object... args) {
        super(errorMessage.getErrorId().concat(" - ").concat(errorMessage.getErrorMessage()));
        apiError = ApiError.builder()
                .errorId(errorMessage.getErrorId())
                .errorMessage(String.format(message, args))
                .status(errorMessage.getStatus())
                .build();
    }

    public BadRequestException(ErrorMessage errorMessage, Map<String, String> additionalData) {
        super(errorMessage.getErrorId().concat(" - ").concat(errorMessage.getErrorMessage()));
        apiError = ApiError.builder()
                .errorId(errorMessage.getErrorId())
                .errorMessage(errorMessage.getErrorMessage())
                .status(errorMessage.getStatus())
                .data(additionalData)
                .build();
    }
}
