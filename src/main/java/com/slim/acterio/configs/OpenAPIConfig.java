package com.slim.acterio.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI().info(infoAPI());
    }

    public Info infoAPI() {
        return new Info().title("Acterio").description("Backend Coding Challenge").contact(contactAPI());
    }

    public Contact contactAPI() {
        Contact contact = new Contact().name("Slim Derouiche").email("slim.derouiche@outlook.com").url("https://github.com/slim20000");
        return contact;
    }
}