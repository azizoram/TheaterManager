package cz.cvut.fel.perfomanceservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PerfomanceserviceApplicationTests {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Test
    public void testWebClient() {
        WebClient webClient = webClientBuilder.build();
        String response = webClient.get()
                .uri("http://example.com")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        assertNotNull(response);
    }
}
