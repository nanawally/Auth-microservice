package com.nanawally.Auth_microservice.advice;

import com.nanawally.Auth_microservice.advice.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.View;

import java.net.ConnectException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final View error;

    public GlobalExceptionHandler(View error) {
        this.error = error;
    }

    private ResponseEntity<ErrorResponseBody> buildResponse(
            HttpStatus status,
            String error,
            String message,
            HttpServletRequest request
    ) {
        ErrorResponseBody apiError = new ErrorResponseBody(
                status.value(),
                error,
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(apiError);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseBody> handleUserNotFoundException(UserNotFoundException e, HttpServletRequest request) {
        logger.warn("User Not Found: {}", e.getMessage());
        return buildResponse(
                HttpStatus.NOT_FOUND,
                "User Not Found",
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseBody> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        logger.error("Unexpected Runtime Exception: ", e);

        if (e.getCause() instanceof ConnectException) {
            logger.warn("Connect Exception: {}", e.getMessage());
            return buildResponse(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Unable To Connect to RabbitMQ",
                    e.getMessage(),
                    request
            );
        }

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Runtime Exception",
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseBody> handleUsernameNotFoundException(UsernameNotFoundException e, HttpServletRequest request) {
        logger.warn("Username Not Found: {}", e.getMessage());
        return buildResponse(
                HttpStatus.NOT_FOUND,
                "Username Not Found",
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseBody> handleDataIntegrityViolationException(DataIntegrityViolationException e, HttpServletRequest request) {
        logger.warn("Data Integrity Violation: {}", e.getMessage());
        return buildResponse(
                HttpStatus.CONFLICT,
                "Data Integrity Violation",
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseBody> handleBadCredentialsException(BadCredentialsException e, HttpServletRequest request) {
        logger.warn("Bad Credentials Exception: {}", e.getMessage());
        return buildResponse(
                HttpStatus.UNAUTHORIZED,
                "Bad Credentials",
                e.getMessage(),
                request
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseBody> handleValidationException(
            MethodArgumentNotValidException e,
            HttpServletRequest request) {

        String errors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .reduce((a, b) -> a + "; " + b)
                .orElse("Validation error");

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Validation Error",
                errors,
                request
        );
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<ErrorResponseBody> handleConnectException(ConnectException e, HttpServletRequest request) {
        logger.warn("Connect Exception: {}", e.getMessage());
        return buildResponse(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Unable To Connect to RabbitMQ",
                e.getMessage(),
                request
        );
    }

}
