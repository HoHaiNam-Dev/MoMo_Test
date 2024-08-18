package com.momo_homework.data_pipeline.utils.error.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@AllArgsConstructor
public class ApiError {
    private String errorId;
    private String errorMessage;
    private HttpStatus status;
    private Object data;

    @Override
    public String toString() {
        return "ApiError{" +
               "errorId='" + errorId + '\'' +
               ", errorMessage='" + errorMessage + '\'' +
               ", status=" + status +
               ", data=" + data +
               '}';
    }
}
