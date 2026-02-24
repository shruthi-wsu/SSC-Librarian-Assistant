package com.example.librarianassistant.dto;

import com.example.librarianassistant.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Authentication response containing JWT token and user details")
@Data
@Builder
public class AuthResponse {

    @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Token type – always 'Bearer'", example = "Bearer")
    @Builder.Default
    private String type = "Bearer";

    @Schema(description = "Internal user ID", example = "1")
    private Long userId;

    @Schema(description = "User's full name", example = "Alice Smith")
    private String name;

    @Schema(description = "User's email address", example = "alice@library.wsu.edu")
    private String email;

    @Schema(description = "Account role", example = "PATRON", allowableValues = {"PATRON", "LIBRARIAN"})
    private User.Role role;
}
