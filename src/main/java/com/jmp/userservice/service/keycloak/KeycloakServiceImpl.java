package com.jmp.userservice.service.keycloak;

import com.jmp.userservice.constant.KeycloakRole;
import com.jmp.userservice.exception.model.KeycloakException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakServiceImpl implements KeycloakService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    @Override
    public void createUser(UUID id, String email, String password) {
        UserRepresentation user = new UserRepresentation();
        user.setEmail(email);
        user.setUsername(id.toString());
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setCredentials(Collections.singletonList(createPassword(password)));

        UsersResource userResource = keycloak.realm(realm).users();

        try (Response response = userResource.create(user)) {
            if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
                throw new KeycloakException("Failed to create user, status: " + response.getStatus());
            }
        }

        addRoleToUser(getKeycloakUserIdByUUID(id).getId(), KeycloakRole.ROLE_USER.name());
    }

    @Override
    public void deleteUser(UUID id) {
        UsersResource userResource = keycloak.realm(realm).users();

        try (Response response = userResource.delete(getKeycloakUserIdByUUID(id).getId())) {
            if (response.getStatus() != Response.Status.NO_CONTENT.getStatusCode()) {
                throw new KeycloakException("Failed to delete user, status: " + response.getStatus());
            }
        }
    }

    @Override
    public UserRepresentation getUser(UUID id) {
        return getKeycloakUserIdByUUID(id);
    }

    private CredentialRepresentation createPassword(String password) {
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(password);
        cred.setTemporary(false);
        return cred;
    }

    private UserRepresentation getKeycloakUserIdByUUID(UUID uuid) {
        UsersResource usersResource = keycloak.realm(realm).users();
        List<UserRepresentation> users = usersResource.search(uuid.toString(), true);

        if (users.isEmpty()){
            log.error("Keycloak user not found, UUID : {}", uuid);
            throw new KeycloakException("User not found with UUID : " + uuid);
        }

        return users.getFirst();
    }

    private void addRoleToUser(String userId, String roleName) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        UserResource userResource = usersResource.get(userId);

        RoleRepresentation role = realmResource.roles().get(roleName).toRepresentation();

        if (role == null) {
            log.error("Role not found : {}", roleName);
            throw new KeycloakException("Role " + roleName + " not found");
        }

        log.info("Adding role {} to user {}", roleName, userId);
        userResource.roles().realmLevel().add(Collections.singletonList(role));
    }

}
