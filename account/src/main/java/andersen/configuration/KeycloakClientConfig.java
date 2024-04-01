package andersen.configuration;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class KeycloakClientConfig {

    private final KeycloakProperties keycloakProperties;

    @Bean
    public Keycloak keycloak() {
        return Keycloak.getInstance(keycloakProperties.getBaseUrl(), keycloakProperties.getRealm(), keycloakProperties.getUsername(),
                keycloakProperties.getPassword(), keycloakProperties.getClientId());
    }

    @Bean
    public WebClient keycloakRestClient() {
        return WebClient.builder()
                .baseUrl(keycloakProperties.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

}
