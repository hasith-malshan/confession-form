package com.anonconfess.backend.dto;

import com.anonconfess.backend.entity.ReactionType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateReactionRequest {

    @NotNull
    private ReactionType reactionType;
}