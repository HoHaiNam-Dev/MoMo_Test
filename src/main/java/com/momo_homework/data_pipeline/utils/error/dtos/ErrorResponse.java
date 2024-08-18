package com.momo_homework.data_pipeline.utils.error.dtos;

import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Objects;

@Getter
@Setter
public class ErrorResponse {
    private String message;
    private Object description;
    private Integer status;
    private Object data;
    private String timestamp;
    private String path;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ErrorResponse(String path, Object message) {
        if (message instanceof ApiError apiError) {
            this.message = apiError.getErrorId();
            this.description = apiError.getErrorMessage();
            this.status = apiError.getStatus().value();
            if (Objects.nonNull(apiError.getData())) {
                this.data = apiError.getData();
            }
        } else {
            this.description = message.toString();
        }
        this.timestamp = sdf.format(System.currentTimeMillis());
        this.path = path;
    }
}
