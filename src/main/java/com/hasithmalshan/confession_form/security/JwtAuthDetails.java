package com.hasithmalshan.confession_form.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtAuthDetails {
    private final Long userId;
    private final String username;
}
