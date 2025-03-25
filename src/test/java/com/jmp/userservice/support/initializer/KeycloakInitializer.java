package com.jmp.userservice.support.initializer;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class KeycloakInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final String KEYCLOAK_IMAGE_NAME = "quay.io/keycloak/keycloak:latest";

    @Container
    public static final KeycloakContainer KEYCLOAK_CONTAINER = new KeycloakContainer(KEYCLOAK_IMAGE_NAME)
            .withExposedPorts(8080)
            .withEnv("KEYCLOAK_ADMIN", "admin")
            .withEnv("KEYCLOAK_ADMIN_PASSWORD", "admin")
            .withRealmImportFile("/keycloak/marketplace-realm.json");


    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        KEYCLOAK_CONTAINER.start();

        TestPropertyValues.of(
                "spring.security.oauth2.resourceserver.jwt.issuer-uri=http://" + KEYCLOAK_CONTAINER.getHost() +
                        ":" + KEYCLOAK_CONTAINER.getHttpPort() + "/realms/marketplace",
                "keycloak.port=" + KEYCLOAK_CONTAINER.getHttpPort(),
                "keycloak.auth-server-url=http://" + KEYCLOAK_CONTAINER.getHost() + ":" + KEYCLOAK_CONTAINER.getHttpPort()
        ).applyTo(applicationContext.getEnvironment());
    }
}
