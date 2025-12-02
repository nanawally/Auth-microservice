package com.nanawally.Auth_microservice.advice;

import com.nanawally.Auth_microservice.advice.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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
    public ResponseEntity<ErrorResponseBody> handleTaskNotFoundException(UserNotFoundException e, HttpServletRequest request) {
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
}
