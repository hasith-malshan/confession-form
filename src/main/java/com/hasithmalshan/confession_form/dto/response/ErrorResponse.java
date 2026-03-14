package com.hasithmalshan.confession_form.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private boolean success;
    private String message;
    private String errorCode;
    private int statusCode;
    private String path;
    private List<FieldError> errors;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    public static ErrorResponse of(String message, String errorCode, int statusCode, String path) {
        return ErrorResponse.builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .statusCode(statusCode)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ErrorResponse of(String message, String errorCode, int statusCode, String path, List<FieldError> errors) {
        return ErrorResponse.builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .statusCode(statusCode)
                .path(path)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
