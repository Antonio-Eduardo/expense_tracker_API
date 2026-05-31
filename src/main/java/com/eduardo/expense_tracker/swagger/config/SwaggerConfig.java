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
                        .description("API REST para controle de gastos pessoais.\n" +
                                "            \n" +
                                "            ## Funcionalidades\n" +
                                "            - Gerenciamento de usuários e perfis\n" +
                                "            - Controle de contas bancárias\n" +
                                "            - Categorização de despesas\n" +
                                "            - Histórico mensal de gastos\n" +
                                "            \n" +
                                "            ## Autenticação\n" +
                                "            Todos os endpoints (exceto /auth) requerem Bearer Token JWT.\n" +
                                "            Para gerar o token, " + "Registre um Email - Senha - Role, no endpoint POST /auth/register.\n" +
                                "            Para receber o token, " + "Faça Login com o usuário registrado no endpoint POST /auth/login.")
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