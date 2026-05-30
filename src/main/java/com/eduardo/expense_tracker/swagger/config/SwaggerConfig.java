package com.eduardo.expense_tracker.swagger.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Expense Tracker API")
                        .version("1.0.0")
                        .description("API REST de controle de gastos pessoais com contas bancárias, categorias de gastos " +
                                "e histórico mensal. Autenticação via JWT, persistência em PostgreSQL, testes de integração com Testcontainers " +
                                "e testes unitários com Mockito. Deploy ativo no Railway.")
                        .contact(new Contact()
                                .name("Antonio Eduardo")
                                .email("eduardo.moreira.java@gmail.com")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}