package com.example.librarianassistant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "Credentials for user login")
@Data
public class LoginRequest {

    @Schema(description = "Registered email address", example = "alice@library.wsu.edu")
    @NotBlank
    @Email
    private String email;

    @Schema(description = "Account password", example = "secret123")
    @NotBlank
    private String password;
}
