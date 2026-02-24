package com.example.librarianassistant.dto;

import com.example.librarianassistant.model.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

    private String token;

    @Builder.Default
    private String type = "Bearer";

    private Long userId;
    private String name;
    private String email;
    private User.Role role;
}
