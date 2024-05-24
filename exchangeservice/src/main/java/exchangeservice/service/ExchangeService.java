package exchangeservice.service;

import exchangeservice.dto.ShiftRequestDTO;
import exchangeservice.model.ExchangeDirect;
import exchangeservice.model.ExchangeRequest;
import exchangeservice.repository.ExchangeRequestRepository;
import exchangeservice.repository.ExchangeDirectRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import exchangeservice.dto.WorkShiftDTO;

import java.util.List;
import java.util.Map;

@Service
public class ExchangeService {
    private final ExchangeDirectRepository directRepository;
    private final ExchangeRequestRepository requestRepository;

    private final WebClient.Builder webClient;
    private static final String workshift_api = "http://workshift/api/";

    public ExchangeService(ExchangeDirectRepository directRepository, ExchangeRequestRepository requestRepository, WebClient.Builder webClientBuilder) {
        this.directRepository = directRepository;
        this.requestRepository = requestRepository;
        this.webClient = webClientBuilder;
    }
    public String requestExchange(Long shiftid, ShiftRequestDTO body) {
        String verificationResult = verifyAuthorIsOnShift(body, shiftid);
        if (!verificationResult.equals("OK")) {
            return verificationResult;
        }

        if (body.getConsumer() == null){
            requestRepository.save(new ExchangeRequest(shiftid, body.getAuthorId(), body.getRole()));
        } else {
            directRepository.save(new ExchangeDirect(shiftid, body.getAuthorId(), body.getRole(), body.getConsumer()));
        }
        return "Your request was successfully made!";
    }

    private String verifyAuthorIsOnShift(ShiftRequestDTO body, Long shiftid) {
        body.setShiftId(shiftid);
        return webClient.build()
                .post()
                .uri(workshift_api + "/verifyRequest")
                .body(Mono.just(body), ShiftRequestDTO.class)
                .retrieve().bodyToMono(String.class).block();
    }

    public List<ShiftRequestDTO> getAllRequests() {
        return requestRepository.findAll().stream().map(ShiftRequestDTO::new).toList();
    }

    public List<ShiftRequestDTO> getRequestsByConsumer(Long consumerId) {
        return requestRepository.findAllByConsumer(consumerId).stream().map(ShiftRequestDTO::new).toList();
    }

    public List<ShiftRequestDTO> getRequestsByAuthor(Long authorId) {
        return requestRepository.findAllByAuthorId(authorId).stream().map(ShiftRequestDTO::new).toList();
    }

    public String confirmExchange(Long requestId) {
        ExchangeRequest request = requestRepository.findById(requestId).orElse(null);
        if (request == null) {
            return "Request not found";
        }
        Long shiftId = request.getShiftId();
        Map<Long, String> author = Map.of(request.getAuthorId(), request.getRole());
        WorkShiftDTO workShiftDTO = new WorkShiftDTO();
        workShiftDTO.setEmployees(author);

        try {
            // Send POST request to update work shift
            webClient.build()
                    .post()
                    .uri(workshift_api + "/update/" + shiftId)
                    .body(BodyInserters.fromValue(workShiftDTO))
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            requestRepository.delete(request);
            return "Exchange confirmed!";
        } catch (Exception e) {
            return "Failed to confirm exchange: " + e.getMessage();
        }
    }
}
