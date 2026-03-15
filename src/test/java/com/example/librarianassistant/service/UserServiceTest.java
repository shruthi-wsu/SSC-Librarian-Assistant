package com.example.librarianassistant.service;

import com.example.librarianassistant.dto.AuthResponse;
import com.example.librarianassistant.dto.LoginRequest;
import com.example.librarianassistant.dto.RegisterRequest;
import com.example.librarianassistant.exception.BusinessException;
import com.example.librarianassistant.model.User;
import com.example.librarianassistant.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private RegisterRequest validRegister;

    @BeforeEach
    void setUp() {
        validRegister = new RegisterRequest();
        validRegister.setName("Alice");
        validRegister.setEmail("alice@test.com");
        validRegister.setPassword("password123");
        validRegister.setRole(User.Role.PATRON);
    }

    @Test
    void register_createsUserAndReturnsToken() {
        AuthResponse response = userService.register(validRegister);

        assertThat(response.getToken()).isNotBlank();
        assertThat(response.getEmail()).isEqualTo("alice@test.com");
        assertThat(userRepository.findByEmail("alice@test.com")).isPresent();
    }

    @Test
    void register_throwsOnDuplicateEmail() {
        userService.register(validRegister);

        assertThatThrownBy(() -> userService.register(validRegister))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Email already registered");
    }

    @Test
    void login_returnsToken() {
        userService.register(validRegister);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("alice@test.com");
        loginRequest.setPassword("password123");

        AuthResponse response = userService.login(loginRequest);

        assertThat(response.getToken()).isNotBlank();
        assertThat(response.getEmail()).isEqualTo("alice@test.com");
    }

    @Test
    void getUserById_returnsCorrectUser() {
        AuthResponse reg = userService.register(validRegister);
        var user = userService.getUserById(reg.getUserId());

        assertThat(user.getName()).isEqualTo("Alice");
        assertThat(user.getEmail()).isEqualTo("alice@test.com");
    }

    @Test
    void updateUserStatus_changesSuspendedStatus() {
        AuthResponse reg = userService.register(validRegister);
        var updated = userService.updateUserStatus(reg.getUserId(), User.UserStatus.SUSPENDED);

        assertThat(updated.getStatus()).isEqualTo(User.UserStatus.SUSPENDED);
    }
}
