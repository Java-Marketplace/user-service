package com.jmp.userservice.support.config;

import com.jmp.userservice.support.initializer.KeycloakInitializer;
import com.jmp.userservice.support.util.AuthUtil;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class UtilConfig {
    private static final String REALM = "marketplace";
    private static final String CLIENT_ID = "user-service";
    private static final String CLIENT_SECRET = "**********";
    private static final String DEFAULT_PASSWORD = "1234";

    @Bean
    public AuthUtil authUtil() {
        String keycloakUrl = KeycloakInitializer.KEYCLOAK_CONTAINER.getAuthServerUrl();
        return new AuthUtil(keycloakUrl, REALM, CLIENT_ID, CLIENT_SECRET, DEFAULT_PASSWORD);
    }

}
