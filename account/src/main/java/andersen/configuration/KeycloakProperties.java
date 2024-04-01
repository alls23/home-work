package andersen.configuration;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {

    private String username;
    private String password;
    private String realm;
    private String clientId;
    private String grantTypePassword;
    private String secret;
    private String baseUrl;
    private String loginUrl;

}
