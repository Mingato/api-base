package com.netagentciadigital.api.commons.exceptions.handler;

import com.netagentciadigital.api.commons.exceptions.DataNotFoundException;
import com.netagentciadigital.api.model.response.ApiResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleValidateException(Exception ex, WebRequest request) {
        Set<ConstraintViolation<?>> violations = ((ConstraintViolationException)ex).getConstraintViolations();
        StringBuilder builder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            builder.append(violation.getPropertyPath() + " " + violation.getMessage());
            break;
        }

        log.error(ex.getMessage(), ex);

        ApiResponseBody result = ApiResponseBody.builder()
                .status("400")
                .message(violations.isEmpty()? ex.getClass().getSimpleName() : builder.toString())
                .build();

        return handleExceptionInternal(ex, result, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

    }

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
        List<FieldError> violations = ex.getFieldErrors();
        StringBuilder builder = new StringBuilder();
        for (FieldError violation : violations) {
            builder.append(violation.getField() + " " + violation.getDefaultMessage());
            break;
        }

        log.error(ex.getMessage(), ex);

        ApiResponseBody result = ApiResponseBody.builder()
                .status("400")
                .message(violations.isEmpty()? ex.getClass().getSimpleName() : builder.toString())
                .build();

        return handleExceptionInternal(ex, result, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

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
