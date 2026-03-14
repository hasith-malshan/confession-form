package com.hasithmalshan.confession_form.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldError {

    private String field;
    private String message;
    private Object rejectedValue;

    public static FieldError of(String field, String message) {
        return FieldError.builder()
                .field(field)
                .message(message)
                .build();
    }

    public static FieldError of(String field, String message, Object rejectedValue) {
        return FieldError.builder()
                .field(field)
                .message(message)
                .rejectedValue(rejectedValue)
                .build();
    }
}
