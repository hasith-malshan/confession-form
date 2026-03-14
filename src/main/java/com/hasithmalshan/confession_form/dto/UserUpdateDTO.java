package com.hasithmalshan.confession_form.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
    
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String fname;
    
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lname;
    
    @Email(message = "Invalid email format")
    private String email;
    
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    private String mobileNo;
    
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
