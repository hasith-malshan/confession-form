package com.hasithmalshan.confession_form.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {
    private String username;
    private String fname;
    private String lname;
    private String email;
    private String mobileNo;
    private String password;
}

