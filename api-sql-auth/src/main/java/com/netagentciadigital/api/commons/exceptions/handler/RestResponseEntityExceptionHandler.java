package com.netagentciadigital.api.commons.exceptions.handler;

import com.netagentciadigital.api.commons.exceptions.DataNotFoundException;
import com.netagentciadigital.api.model.response.ApiResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {DataNotFoundException.class})
    protected ResponseEntity<Object> handleDataNotFound(Exception ex, WebRequest request) {
        return generateObjectResponseEntity(ex, request, "404", HttpStatus.OK);
    }

    @ExceptionHandler(value = {RuntimeException.class, Exception.class})
    protected ResponseEntity<Object> handleInternalServerError(Exception ex, WebRequest request) {
        return generateObjectResponseEntity(ex, request, "500", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return generateObjectResponseEntity(ex, request, "400", HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return generateObjectResponseEntity(ex, request, "400", HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return generateObjectResponseEntity(ex, request, "400", HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> generateObjectResponseEntity(Exception ex, WebRequest request, String s, HttpStatus internalServerError) {
        log.error(ex.getMessage(), ex);

        ApiResponseBody result = ApiResponseBody.builder()
                .status(s)
                .message(ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage())
                .build();
        return handleExceptionInternal(ex, result, new HttpHeaders(), internalServerError, request);
    }


}
