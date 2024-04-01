package andersen.service;

import andersen.configuration.KeycloakProperties;
import andersen.dto.request.UserRegistrationRequest;
import andersen.dto.response.UserLoginResponse;
import jakarta.ws.rs.core.Response;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final Keycloak instance;
    private final KeycloakProperties keycloakProperties;
    private final WebClient webClient;

    public Mono<String> createAccount(UserRegistrationRequest request) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEnabled(true);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType("password");
        credentialRepresentation.setValue(request.getPassword());
        credentialRepresentation.setTemporary(false);

        user.setCredentials(Collections.singletonList(credentialRepresentation));

        return Mono.just(instance.realm(keycloakProperties.getRealm()).users().create(user))
                .doOnSuccess(response -> log.info("Response |  Status: {} | Status Info: {}", response.getStatus(), response.getStatusInfo()))
                .map(response -> response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1"));
    }

    public Mono<Object> login() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", keycloakProperties.getUsername());
        params.add("password", keycloakProperties.getPassword());
        params.add("grant_type", keycloakProperties.getGrantTypePassword());
        params.add("client_id", keycloakProperties.getClientId());
        params.add("client_secret", keycloakProperties.getSecret());
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path(keycloakProperties.getLoginUrl()).build())
                .bodyValue(params)
                .retrieve()
                .bodyToMono(Object.class)
                .doOnSuccess(response -> log.info("Response | {}", response))
                .doOnError(throwable -> log.error("Error | {}", throwable.getMessage()));
    }
}
