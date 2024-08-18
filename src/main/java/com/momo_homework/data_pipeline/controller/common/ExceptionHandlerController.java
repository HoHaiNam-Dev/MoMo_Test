package com.momo_homework.data_pipeline.controller.common;


import com.momo_homework.data_pipeline.utils.error.dtos.ErrorResponse;
import com.momo_homework.data_pipeline.utils.error.exception.BadRequestException;
import com.momo_homework.data_pipeline.utils.error.exception.FileUploadException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {
    private static final String ERROR_LOG_FORMAT = "Error: URI: {}, ErrorCode: {}, Message: {}";

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handelBadRequestException(BadRequestException e, HttpServletRequest request) {
        Object exMessage = e.getApiError() == null ? e.getMessage() : e.getApiError();
        log.error(ERROR_LOG_FORMAT, request.getRequestURI(), HttpStatus.BAD_REQUEST, exMessage);
        log.debug("Error: ", e);
        return new ErrorResponse(request.getRequestURI(), exMessage);
    }

    @ExceptionHandler(FileUploadException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handelFileUploadException(FileUploadException e, HttpServletRequest request) {
        Object exMessage = e.getApiError() == null ? e.getMessage() : e.getApiError();
        log.error(ERROR_LOG_FORMAT, request.getRequestURI(), HttpStatus.BAD_REQUEST, exMessage);
        log.debug("Error: ", e);
        return new ErrorResponse(request.getRequestURI(), exMessage);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handelHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String exMessage = e.getMessage();
        log.error(ERROR_LOG_FORMAT, request.getRequestURI(), HttpStatus.BAD_REQUEST, exMessage);
        log.debug("Error: ", e);
        return new ErrorResponse(request.getRequestURI(), exMessage);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e, HttpServletRequest request) {
        log.error("message = {}", e);
        String exMessage = e.getMessage();
        log.error(ERROR_LOG_FORMAT, request.getRequestURI(), HttpStatus.BAD_REQUEST, exMessage);
        log.debug("Error: ", e);
        return new ErrorResponse(request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }


}
