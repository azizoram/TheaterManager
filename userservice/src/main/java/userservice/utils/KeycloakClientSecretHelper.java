package userservice.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
public class KeycloakClientSecretHelper {
    private final WebClient.Builder webClientBuilder;
    private final String keycloakUrl;
    private final String realmName;
    private final String clientId;
    private final KeycloakAdminProperties adminProperties;

    public KeycloakClientSecretHelper(String keycloakUrl, String realmName, String clientId, WebClient.Builder webClientBuilder, KeycloakAdminProperties adminProperties) {
        this.webClientBuilder = webClientBuilder;
        this.keycloakUrl = keycloakUrl;
        this.realmName = realmName;
        this.clientId = clientId;
        this.adminProperties = adminProperties;
    }

    public static String retrieveSecretKey(String keycloakUrl, String realmName, String clientId,WebClient.Builder webClientBuilder, KeycloakAdminProperties adminProperties) {
        KeycloakClientSecretHelper helper = new KeycloakClientSecretHelper(keycloakUrl, realmName, clientId, webClientBuilder, adminProperties);
        return helper.retrieveClientSecret();
    }

    public String retrieveClientSecret() {
        // Prepare WebClient
        WebClient webClient = WebClient.builder()
                .baseUrl(keycloakUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String adminToken = webClient.post()
                .uri("/realms/master/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "password")
                        .with("client_id", adminProperties.getClientId())
                        .with("username", adminProperties.getUsername())
                        .with("password", adminProperties.getPassword()))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Parse token to get access token
        String accessToken = "";
        if (adminToken != null) {
            accessToken = JsonParserFactory.getJsonParser().parseMap(adminToken).get("access_token").toString();
        }

        // Retrieve client information
        String clientInfo = webClient.get()
                .uri("/admin/realms/{realm}/clients/", realmName)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        String clientSecret = parseClientSecret(clientInfo, clientId);

        return clientSecret;
    }

    private String parseClientSecret(String responseBody, String clientId) {
        String result = JsonParserFactory.getJsonParser().parseList(responseBody).stream()
                .filter(client -> clientId.equals(((Map<String, String>) client).get("clientId")))
                .map(client -> ((Map<String, String>) client).get("secret"))
                .findFirst()
                .orElse(null);

        return result == null ? "TOKEN IS NOT ACQUIRED" : result;
    }
}