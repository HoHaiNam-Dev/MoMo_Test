package com.momo_homework.data_pipeline.controller.common;


import com.momo_homework.data_pipeline.utils.error.dtos.ErrorResponse;
import com.momo_homework.data_pipeline.utils.error.exception.BadRequestException;
import com.momo_homework.data_pipeline.utils.error.exception.FileUploadException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {
    private static final String ERROR_LOG_FORMAT = "Error: URI: {}, ErrorCode: {}, Message: {}";

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handelBadRequestException(BadRequestException e, HttpServletRequest request) {
        Object exMessage = e.getApiError() == null ? e.getMessage() : e.getApiError();
        log.error(ERROR_LOG_FORMAT, request.getRequestURI(), HttpStatus.BAD_REQUEST, exMessage, e);
        log.debug("Error: ", e);
        return new ErrorResponse(request.getRequestURI(), exMessage);
    }

    @ExceptionHandler(FileUploadException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handelFileUploadException(FileUploadException e, HttpServletRequest request) {
        Object exMessage = e.getApiError() == null ? e.getMessage() : e.getApiError();
        log.error(ERROR_LOG_FORMAT, request.getRequestURI(), HttpStatus.BAD_REQUEST, exMessage, e);
        log.debug("Error: ", e);
        return new ErrorResponse(request.getRequestURI(), exMessage);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handelHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String exMessage = e.getMessage();
        log.error(ERROR_LOG_FORMAT, request.getRequestURI(), HttpStatus.BAD_REQUEST, exMessage, e);
        log.debug("Error: ", e);
        return new ErrorResponse(request.getRequestURI(), exMessage);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e, HttpServletRequest request) {
        String exMessage = e.getMessage();
        log.error(ERROR_LOG_FORMAT, request.getRequestURI(), HttpStatus.BAD_REQUEST, exMessage, e);
        log.debug("Error: ", e);
        return new ErrorResponse(request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e, HttpServletRequest request) {
        String exMessage = e.getMessage();
        log.error(ERROR_LOG_FORMAT, request.getRequestURI(), HttpStatus.BAD_REQUEST, exMessage, e);
        log.debug("Error: ", e);
        return new ErrorResponse(request.getRequestURI(), exMessage);
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleIOException(IOException e, HttpServletRequest request) {
        String exMessage = e.getMessage();
        log.error(ERROR_LOG_FORMAT, request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR, exMessage, e);
        log.debug("Error: ", e);
        return new ErrorResponse(request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleArrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException e, HttpServletRequest request) {
        String exMessage = e.getMessage();
        log.error(ERROR_LOG_FORMAT, request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR, exMessage, e);
        log.debug("Error: ", e);
        return new ErrorResponse(request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleNullPointerException(NullPointerException e, HttpServletRequest request) {
        String exMessage = e.getMessage();
        log.error(ERROR_LOG_FORMAT, request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR, exMessage, e);
        log.debug("Error: ", e);
        return new ErrorResponse(request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    @ExceptionHandler(InterruptedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInterruptedException(InterruptedException e, HttpServletRequest request) {
        String exMessage = e.getMessage();
        log.error(ERROR_LOG_FORMAT, request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR, exMessage, e);
        log.debug("Error: ", e);
        return new ErrorResponse(request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }
}
