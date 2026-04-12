package com.project.hotel_svc.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final Logger log = LoggerFactory.getLogger(OpenApiConfig.class);

    @Bean
    public OpenAPI userServiceOpenAPI() {
        try {
            return new OpenAPI()
                    .info(new Info()
                            .title("Hotel Service API")
                            .description("Hotel Management Microservice for Hotel Booking System")
                            .version("1.0.0")
                            .contact(new Contact()
                                    .name("Dipak Das")
                                    .email("dipak628das@gmail.com"))
                            .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                    )
                    // Add JWT security globally
                    .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                    .components(new Components()
                            .addSecuritySchemes("bearerAuth",
                                    new SecurityScheme()
                                            .name("Authorization")
                                            .type(SecurityScheme.Type.HTTP)
                                            .scheme("bearer")
                                            .bearerFormat("JWT")
                            )
                    );
        } catch (Exception ex) {
            // Defensive fallback so that OpenAPI generation errors don't take down the app
            log.error("Failed to build OpenAPI spec, returning minimal spec", ex);
            return new OpenAPI().info(new Info().title("User Service API").version("unknown"));
        }
    }
}