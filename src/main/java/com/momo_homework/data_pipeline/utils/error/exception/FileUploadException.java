package com.momo_homework.data_pipeline.utils.error.exception;

import com.momo_homework.data_pipeline.utils.error.dtos.ApiError;
import com.momo_homework.data_pipeline.utils.error.dtos.ErrorMessage;
import lombok.Getter;

@Getter
public class FileUploadException extends RuntimeException {
    private ApiError apiError;

    public FileUploadException(String errorMessage, Object... args) {
        super(String.format(errorMessage, args));
    }

    public FileUploadException(ErrorMessage errorMessage) {
        apiError = ApiError.builder()
                .errorId(errorMessage.getErrorId())
                .errorMessage(errorMessage.getErrorMessage())
                .status(errorMessage.getStatus())
                .data(null)
                .build();
    }
}
