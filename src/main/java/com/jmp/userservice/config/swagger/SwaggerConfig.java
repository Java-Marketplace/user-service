package com.jmp.userservice.config.swagger;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "User Service",
                description = "Данный сервис отвечает за управление профилями пользователей"
        ),
        servers = {
                @Server(
                        description = "Локальное окружение",
                        url = "http://localhost:${server.port}"
                ),
        },
        security = @SecurityRequirement(name = "keycloak_oauth_scheme")
)
@SecurityScheme(
        name = "keycloak_oauth_scheme",
        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(
                password = @OAuthFlow(
                        tokenUrl = "${keycloak.token-url}",
                        refreshUrl = "${keycloak.refresh-url}",
                        authorizationUrl = "${keycloak.authorization-url}"
                )
        )
)
public class SwaggerConfig {
}
