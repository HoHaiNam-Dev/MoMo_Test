package com.momo_homework.data_pipeline.utils.error.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
    /* Error Code structure
     * 1st Block: Service Name: Mobile or Watch
     * 2nd Block: Http Code: 400, 401, 404, 500, 503
     * 3rd Block: Feature code: follow Figma
     */

    /* File Errors */
    FILE_DOES_NOT_EXIST("FILE-404-0001", "File does not exist", HttpStatus.NOT_FOUND),
    FILE_IS_EMPTY("FILE-404-0002", "File is empty", HttpStatus.NOT_FOUND),
    FILE_TYPE_INCORRECT("FILE-400-0003", "File type is incorrect", HttpStatus.BAD_REQUEST),
    FILE_PATH_INVALID("FILE-400-0004", "File path is invalid", HttpStatus.BAD_REQUEST),
    ;

    private final String errorId;
    private final String errorMessage;
    private final HttpStatus status;

}
