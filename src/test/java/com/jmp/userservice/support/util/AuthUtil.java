package com.jmp.userservice.support.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

@Slf4j
@RequiredArgsConstructor
public class AuthUtil {
    private final String serverUrl;
    private final String realm;
    private final String clientId;
    private final String clientSecret;
    private final String defaultPassword;

    public String getAuthorization(String username, String password) {
        try (Keycloak keycloakAdminClient = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .grantType(OAuth2Constants.PASSWORD)
                .username(username)
                .password(password)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build()
        ) {
            return "Bearer " + keycloakAdminClient.tokenManager().getAccessToken().getToken();
        } catch (Exception e) {
            log.error("Warning when trying to give the token");
            e.getStackTrace();
        }
        return null;
    }

    public String getAuthorization(String username) {
        return this.getAuthorization(username, defaultPassword);
    }

}
