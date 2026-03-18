package com.example.librarianassistant.dto;

import com.example.librarianassistant.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "New user registration payload")
@Data
public class RegisterRequest {

    @Schema(description = "Full name of the user", example = "Alice Smith")
    @NotBlank
    private String name;

    @Schema(description = "Email address (must be unique)", example = "alice@library.wsu.edu")
    @NotBlank
    @Email
    private String email;

    @Schema(description = "Password – minimum 6 characters", example = "secret123")
    @NotBlank
    @Size(min = 6)
    private String password;

    @Schema(description = "Account role – defaults to PATRON", example = "PATRON",
            allowableValues = {"PATRON", "LIBRARIAN"})
    private User.Role role = User.Role.PATRON;
}
