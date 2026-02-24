package com.example.librarianassistant.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String BEARER_SCHEME = "Bearer Authentication";

    @Bean
    public OpenAPI libraryOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Librarian Assistant API")
                        .description("""
                                REST API for the **Librarian Assistant** library management system.

                                ## Authentication
                                Most endpoints require a JWT Bearer token. Use `POST /api/auth/login` \
                                to obtain a token, then click **Authorize** and enter `Bearer <token>`.

                                ## Roles
                                - **LIBRARIAN** – full access (CRUD on books, user management, overdue reports)
                                - **PATRON** – read access to books, own checkouts and holds
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Librarian Assistant Team")
                                .email("team@library.wsu.edu"))
                        .license(new License()
                                .name("MIT License")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME))
                .components(new Components()
                        .addSecuritySchemes(BEARER_SCHEME,
                                new SecurityScheme()
                                        .name(BEARER_SCHEME)
                                        .description("Provide the JWT token obtained from POST /api/auth/login")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
