package com.ticket.ticketreservation.config;

import io.swagger.annotations.*;

@SwaggerDefinition(
        info = @Info(
                description = "Spring Boot REST API for Ticket Reservation",
                version = "V1.0",
                title = "Spring Boot REST API",
                termsOfService = "Terms of service",
                contact = @Contact(name = "Praneeth-Talluri", email = "praneethtalluri04@gmail.com"),
                license = @License(name = "Apache 2.0", url = "http://www.apache.org")
        ),
        consumes = {"application/json" },
        produces = {"application/json" },
        schemes = {SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS},
        externalDocs = @ExternalDocs(value = "About me", url = "http://about.me/me")
)
public interface ApiDocumentationConfig {
}
