package com.example.librarianassistant.controller;

import com.example.librarianassistant.dto.UserResponse;
import com.example.librarianassistant.model.User;
import com.example.librarianassistant.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@Tag(name = "Users", description = "User management – most endpoints require LIBRARIAN role")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "List all users", description = "**Requires LIBRARIAN role.** Returns all registered users.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User list retrieved",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Insufficient role (LIBRARIAN required)", content = @Content)
    })
    @GetMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Get current user profile",
               description = "Returns the profile of the currently authenticated user (derived from the JWT token).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile retrieved",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token", content = @Content)
    })
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Principal principal) {
        return ResponseEntity.ok(userService.getUserByEmail(principal.getName()));
    }

    @Operation(summary = "Get a user by ID", description = "**Requires LIBRARIAN role.**")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Insufficient role (LIBRARIAN required)", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "User ID", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Update user account status",
               description = "**Requires LIBRARIAN role.** Changes a user's status to ACTIVE, SUSPENDED, or INACTIVE.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token", content = @Content),
            @ApiResponse(responseCode = "403", description = "Insufficient role (LIBRARIAN required)", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<UserResponse> updateStatus(
            @Parameter(description = "User ID", example = "1") @PathVariable Long id,
            @Parameter(description = "New status", schema = @Schema(implementation = User.UserStatus.class))
            @RequestParam User.UserStatus status) {
        return ResponseEntity.ok(userService.updateUserStatus(id, status));
    }
}
