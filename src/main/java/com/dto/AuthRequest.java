package com.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class AuthRequest {

    @Size(min = 5, max = 30, message = "Username should contains from 5 to 30 symbols")
    @NotBlank(message = "Username can't be empty")
    private String username;

    @Size(min = 8, max = 20, message = "Password should contains from 8 to 20 symbols")
    @NotBlank(message = "Password can't be empty")
    private String password;
}
