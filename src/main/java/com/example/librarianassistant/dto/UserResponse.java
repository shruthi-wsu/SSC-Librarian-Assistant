package com.example.librarianassistant.dto;

import com.example.librarianassistant.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Schema(description = "User profile returned by the API")
@Data
@Builder
public class UserResponse {

    @Schema(description = "Internal user ID", example = "1")
    private Long id;

    @Schema(description = "Full name", example = "Alice Smith")
    private String name;

    @Schema(description = "Email address", example = "alice@library.wsu.edu")
    private String email;

    @Schema(description = "Account role", example = "PATRON", allowableValues = {"PATRON", "LIBRARIAN"})
    private User.Role role;

    @Schema(description = "Account status", example = "ACTIVE",
            allowableValues = {"ACTIVE", "SUSPENDED", "INACTIVE"})
    private User.UserStatus status;

    @Schema(description = "Date the account was registered", example = "2024-09-01")
    private LocalDate registrationDate;
}
