package com.senla.pricemonitor.controller;

import com.senla.pricemonitor.dto.ResponseErrorDto;
import com.senla.pricemonitor.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseErrorDto> handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        log.warn("NotFoundException: {}", ex.getMessage());
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ResponseErrorDto> handleAuthorizationDeniedException(AuthorizationDeniedException ex, HttpServletRequest request) {
        log.warn("AuthorizationDeniedException: {}", ex.getMessage());
        return buildResponseEntity(HttpStatus.FORBIDDEN, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseErrorDto> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("Validation failed: {}", errors);
        return buildResponseEntity(HttpStatus.BAD_REQUEST, errors, request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseErrorDto> handleException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error", ex);
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера", request.getRequestURI());
    }

    private ResponseEntity<ResponseErrorDto> buildResponseEntity(HttpStatus status, String message, String path) {
        ResponseErrorDto errorDto = ResponseErrorDto.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .build();
        return new ResponseEntity<>(errorDto, status);
    }
}