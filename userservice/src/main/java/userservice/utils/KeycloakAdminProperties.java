package userservice.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "keycloak.admin")
public class KeycloakAdminProperties {
    // Getters and setters
    private String username;
    private String password;
    private String clientId = "admin-cli";


}
