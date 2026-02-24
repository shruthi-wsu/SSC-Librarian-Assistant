package com.example.librarianassistant.controller;

import com.example.librarianassistant.dto.AuthResponse;
import com.example.librarianassistant.dto.LoginRequest;
import com.example.librarianassistant.dto.RegisterRequest;
import com.example.librarianassistant.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Register and login – no token required")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(summary = "Register a new user",
               description = "Creates a PATRON account by default. Pass `role: LIBRARIAN` to create a librarian account.",
               security = {})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registration successful – returns JWT token",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error or email already in use",
                    content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @Operation(summary = "Login and obtain a JWT token",
               description = "Authenticate with email and password. Use the returned token in the `Authorization: Bearer <token>` header.",
               security = {})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful – returns JWT token",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }
}
