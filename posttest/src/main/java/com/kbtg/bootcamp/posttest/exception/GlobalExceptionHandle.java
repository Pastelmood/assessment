package com.kbtg.bootcamp.posttest.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler
    public ResponseEntity<GlobalExceptionResponse> handleException(
            StatusInternalServerErrorException exception) {

        // create a GlobalExceptionResponse
        GlobalExceptionResponse errorResponse = new GlobalExceptionResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setError(HttpStatus.INTERNAL_SERVER_ERROR.name());
        errorResponse.setMessage(exception.getMessage());

        // return ResponseEntity
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<BadRequestExceptionResponse> handleValidateException(
            MethodArgumentNotValidException exception) {

        // get the default errors
        List<String> messages = new ArrayList<>();
        List<FieldError> fieldErrorList = exception.getFieldErrors();

        if (!fieldErrorList.isEmpty()) {
            for (FieldError fieldError : fieldErrorList) {
                messages.add(fieldError.getDefaultMessage());
            }
        }

        // create exception response
        BadRequestExceptionResponse errorResponse = new BadRequestExceptionResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(HttpStatus.BAD_REQUEST.name());
        errorResponse.setMessages(messages);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<GlobalExceptionResponse> handleMethodValidationException(
            HandlerMethodValidationException exception) {

        GlobalExceptionResponse errorResponse = new GlobalExceptionResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(HttpStatus.BAD_REQUEST.name());
        errorResponse.setMessage("Validation failed");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<GlobalExceptionResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException exception) {

        GlobalExceptionResponse errorResponse = new GlobalExceptionResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(HttpStatus.BAD_REQUEST.name());
        errorResponse.setMessage("Validation failed");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
