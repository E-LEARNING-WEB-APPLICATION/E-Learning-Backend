package com.learnease.server.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("LearnEase API Documentation")
                        .version("v1.0")
                        .description("API documentation for the LearnEase E-Learning Platform"))
                // Define the security scheme component
                .components(new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT") // Optional, for documentation purposes
                )
        );
                // Apply the security globally to all operations (optional)
                // Or apply it using annotations on specific controllers/methods
//                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
