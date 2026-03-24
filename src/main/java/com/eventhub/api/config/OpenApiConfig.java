package com.eventhub.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI eventHubOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("EventHub API")
                        .version("1.0.0")
                        .description("REST API for managing events and categories. " +
                                "Supports pagination, filtering, sorting and caching.")
                        .contact(new Contact()
                                .name("EventHub Team")
                                .email("support@eventhub.com"))
                        .license(new License().name("MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Dev Server")));
    }
}
