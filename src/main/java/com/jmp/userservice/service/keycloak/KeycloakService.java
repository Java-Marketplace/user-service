package com.jmp.userservice.service.keycloak;

import org.keycloak.representations.idm.UserRepresentation;

import java.util.UUID;

public interface KeycloakService {
    void createUser(UUID id, String email, String phoneNumber);

    void deleteUser(UUID id);

    UserRepresentation getUser(UUID id);
}
