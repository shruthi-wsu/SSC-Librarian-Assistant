package com.example.librarianassistant.dto;

import com.example.librarianassistant.model.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private User.Role role;
    private User.UserStatus status;
    private LocalDate registrationDate;
}
