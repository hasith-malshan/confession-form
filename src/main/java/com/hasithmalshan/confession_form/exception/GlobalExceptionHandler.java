package com.hasithmalshan.confession_form.exception;

import com.hasithmalshan.confession_form.dto.response.ErrorResponse;
import com.hasithmalshan.confession_form.dto.response.FieldError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {
        log.error("Resource not found: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
                ex.getMessage(),
                "RESOURCE_NOT_FOUND",
                HttpStatus.NOT_FOUND.value(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(
            BadRequestException ex, HttpServletRequest request) {
        log.error("Bad request: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
                ex.getMessage(),
                ex.getErrorCode(),
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(
            DuplicateResourceException ex, HttpServletRequest request) {
        log.error("Duplicate resource: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
                ex.getMessage(),
                "DUPLICATE_RESOURCE",
                HttpStatus.CONFLICT.value(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(
            UnauthorizedException ex, HttpServletRequest request) {
        log.error("Unauthorized: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
                ex.getMessage(),
                ex.getErrorCode(),
                HttpStatus.UNAUTHORIZED.value(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.error("Validation failed: {}", ex.getMessage());

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> FieldError.of(
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getRejectedValue()
                ))
                .collect(Collectors.toList());

        ErrorResponse errorResponse = ErrorResponse.of(
                "Validation failed",
                "VALIDATION_ERROR",
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI(),
                fieldErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(
            BindException ex, HttpServletRequest request) {
        log.error("Binding failed: {}", ex.getMessage());

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> FieldError.of(
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getRejectedValue()
                ))
                .collect(Collectors.toList());

        ErrorResponse errorResponse = ErrorResponse.of(
                "Request binding failed",
                "BINDING_ERROR",
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI(),
                fieldErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpServletRequest request) {
        log.error("Missing parameter: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
                String.format("Required parameter '%s' is missing", ex.getParameterName()),
                "MISSING_PARAMETER",
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        log.error("Type mismatch: {}", ex.getMessage());

        String message = String.format("Parameter '%s' should be of type '%s'",
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");

        ErrorResponse errorResponse = ErrorResponse.of(
                message,
                "TYPE_MISMATCH",
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.error("Message not readable: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
                "Malformed JSON request or invalid data format",
                "MALFORMED_REQUEST",
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        log.error("Method not supported: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
                String.format("HTTP method '%s' is not supported for this endpoint", ex.getMethod()),
                "METHOD_NOT_ALLOWED",
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        log.error("Media type not supported: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
                String.format("Media type '%s' is not supported", ex.getContentType()),
                "UNSUPPORTED_MEDIA_TYPE",
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorResponse);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpServletRequest request) {
        log.error("No handler found: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
                String.format("No endpoint found for %s %s", ex.getHttpMethod(), ex.getRequestURL()),
                "ENDPOINT_NOT_FOUND",
                HttpStatus.NOT_FOUND.value(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(
            BadCredentialsException ex, HttpServletRequest request) {
        log.error("Bad credentials: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
                "Invalid username or password",
                "INVALID_CREDENTIALS",
                HttpStatus.UNAUTHORIZED.value(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            AuthenticationException ex, HttpServletRequest request) {
        log.error("Authentication failed: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
                "Authentication failed",
                "AUTHENTICATION_FAILED",
                HttpStatus.UNAUTHORIZED.value(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex, HttpServletRequest request) {
        log.error("Access denied: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
                "You do not have permission to access this resource",
                "ACCESS_DENIED",
                HttpStatus.FORBIDDEN.value(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, HttpServletRequest request) {
        log.error("Unexpected error occurred: ", ex);

        ErrorResponse errorResponse = ErrorResponse.of(
                "An unexpected error occurred. Please try again later.",
                "INTERNAL_SERVER_ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
