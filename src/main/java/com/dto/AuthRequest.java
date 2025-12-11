package com.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class AuthRequest {


    @Size(min = 10, max = 30, message = "Email should contains from 10 to 30 symbols")
    @NotBlank(message = "Email can't be empty")
    @Email(message = "Email should have a pattern like 'user@some.com'")
    private String email;

    @Size(min = 8, max = 20, message = "Password should contains from 8 to 20 symbols")
    @NotBlank(message = "Password can't be empty")
    private String password;
}
