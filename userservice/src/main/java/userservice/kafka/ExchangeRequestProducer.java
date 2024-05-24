package userservice.kafka;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRequestProducer {

    private final KafkaTemplate<String, ExchangeRequestEvent> kafkaTemplate;

    private static final String TOPIC = "EXCHANGE_REQUEST";

    @Autowired
    public ExchangeRequestProducer(KafkaTemplate<String, ExchangeRequestEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendExchangeRequest(ExchangeRequestEvent event) {
        kafkaTemplate.send("EXCHANGE_REQUEST", event);
    }
}
