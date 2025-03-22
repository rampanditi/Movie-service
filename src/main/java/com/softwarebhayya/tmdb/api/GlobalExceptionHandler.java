package com.softwarebhayya.tmdb.api;

import com.softwarebhayya.tmdb.exception.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.*;

@ControllerAdvice // Changed from @Controller to @ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @Getter
    static class Error {
        private final String reason;
        private final String message;

        Error(String reason, String message) {
            this.reason = reason;
            this.message = message;
        }
    }

    // 400 Bad Request - Invalid Data
    @ExceptionHandler(InvalidDataException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleInvalidException(InvalidDataException ex) {
        log.warn("InvalidDataException: {}", ex.getMessage());
        return new Error(HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage());
    }

    // 400 Bad Request - No Resource Found (like /movies/)
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleNoResourceFound(NoResourceFoundException ex) {
        log.warn("Invalid request path: {}", ex.getResourcePath());
        return new Error(HttpStatus.BAD_REQUEST.getReasonPhrase(), "Invalid request path - ID is required for /movies/");
    }

    // 404 Not Found
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleNotFoundException(NotFoundException ex) {
        log.warn("NotFoundException: {}", ex.getMessage());
        return new Error(HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage());
    }

    // 500 Internal Server Error
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Error handleUnKnownException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return new Error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex.getMessage());
    }
}