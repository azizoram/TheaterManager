package userservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import userservice.exception.UnAuthorizedException;
import userservice.model.User;
import org.springframework.http.HttpStatus;
import userservice.utils.KeycloakAdminProperties;
import userservice.utils.KeycloakClientSecretHelper;

import java.time.Duration;
import java.util.Objects;

@Service
public class AuthorizationService {
    private final UserService userService;
    private final PasswordEncoder encoder;

    private final WebClient.Builder webClientBuilder;

    private final String keycloakUrl = "http://keycloak:8080";
    private final String clientId = "spring-cloud-client";
    private String clientSecret = null;
    private final String realm = "theater-manager-realm";
    private final KeycloakAdminProperties keycloakAdminProperties;
    @Autowired
    AuthorizationService (@Lazy UserService userService, PasswordEncoder encoder, WebClient.Builder webclientBuilder, KeycloakAdminProperties keycloakAdminProperties){
        this.userService = userService;
        this.encoder = encoder;
        this.webClientBuilder = webclientBuilder;
        this.keycloakAdminProperties = keycloakAdminProperties;
        this.clientSecret = KeycloakClientSecretHelper.retrieveSecretKey(keycloakUrl, realm, clientId, this.webClientBuilder, keycloakAdminProperties);

    }



    public String login(String email, String password) throws UnAuthorizedException{
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);
        User user = userService.findByEmail(email);
        Objects.requireNonNull(user);
        if (encoder.matches(password, user.getPassword())){
            return getUserToken(user);
        }
        throw new UnAuthorizedException("Invalid credentials");
    }

    private String getUserToken(User user) {
        // call i vanus
        WebClient webClient = WebClient.builder().baseUrl(keycloakUrl).build();


        String result = webClient.post()
                .uri("/realms/theater-manager-realm/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "client_credentials")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return extractAccessToken(result);
    }

    private String extractAccessToken(String responseBody) {
        // Assuming the responseBody is a JSON object, extract the access token
        // For example, using Jackson:
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.get("access_token").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error extracting access token", e);
        }
    }
}
