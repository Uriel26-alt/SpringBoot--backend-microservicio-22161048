package com.microservicio.tienda.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Microservicio Tienda - Productos y Ventas")
                        .version("1.0.0")
                        .description("Microservicio REST para gestión de productos y ventas con Spring Boot")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("contacto@tienda.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                );
    }
}
