package com.anonconfess.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateConfessionRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String mood;

    @NotBlank
    private String category;

    @NotBlank
    private String text;
}