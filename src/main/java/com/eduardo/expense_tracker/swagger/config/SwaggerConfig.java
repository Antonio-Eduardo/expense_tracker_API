package com.eduardo.expense_tracker.swagger.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${swagger.server.url:}")
    private String serverUrl;


    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openAPI = new OpenAPI()
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
                                "            Todos os endpoints, exceto `/auth/**`, requerem Bearer Token JWT\n\n" +
                                "           ### Como autenticar \n" +
                                "            - Registre um usuário no endpoint `POST /auth/register`\n" +
                                "            - Faça login no endpoint `POST /auth/login`\n" +
                                "            - Copie o token JWT retornado\n" +
                                "            - Clique em Authorize no Swagger\n" +
                                "            - Informe: \n" +
                                "            - Bearer seu-token\n\n"+
                                "           #### Links\n" +
                                "             - [GitHub](https://github.com/Antonio-Eduardo)\n" +
                                "             - [Linkedin](https://www.linkedin.com/in/antonio-eduardo-moreira-oliveira-418828242/)"
                        )
                        .contact(new Contact()
                                .name("Antonio Eduardo")
                                .email("eduardo.moreira.java@gmail.com")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
        if (serverUrl != null && !serverUrl.isEmpty()) {
            openAPI.addServersItem(new Server().url(serverUrl));
        }

        return openAPI;
    }
}