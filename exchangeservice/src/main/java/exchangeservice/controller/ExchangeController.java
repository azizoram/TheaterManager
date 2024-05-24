package exchangeservice.controller;

import exchangeservice.dto.ShiftRequestDTO;
import exchangeservice.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController()
@RequestMapping("/api/exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;
    @Autowired
    public ExchangeController(ExchangeService exchangeService){
        this.exchangeService = exchangeService;
    }
    
    @PostMapping("/request/{shiftid}")
    public ResponseEntity<String> requestExchange(@PathVariable Long shiftid, @RequestBody ShiftRequestDTO body) {
        try {
            return ResponseEntity.ok(exchangeService.requestExchange(shiftid, body));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
//
//    {authorId} {shiftId} {role}
//
//
//    @PostMapping("/requestDirect") {authorId} {shiftId} {role} {consumerId}
    @PostMapping("/requestDirect/{shiftid}")
    public ResponseEntity<String> requestDirectExchange(@PathVariable Long shiftid, @RequestBody ShiftRequestDTO body) {
        try {
            Objects.requireNonNull(body.getConsumer());
            return ResponseEntity.ok(exchangeService.requestExchange(shiftid, body));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ShiftRequestDTO> getAllRequests() {
        return exchangeService.getAllRequests();
    }

    @GetMapping("/requestedTo/{consumerId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ShiftRequestDTO> getAllRequests(@PathVariable Long consumerId) {
        return exchangeService.getRequestsByConsumer(consumerId);
    }

    @GetMapping("/requestedBy/{authorId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ShiftRequestDTO> getAllRequestsByAuthor(@PathVariable Long authorId) {
        return exchangeService.getRequestsByAuthor(authorId);
    }

    @PostMapping("/confirm/{requestId}")
    public ResponseEntity<String> confirmExchange(@PathVariable Long requestId) {
        try {
            return ResponseEntity.ok(exchangeService.confirmExchange(requestId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/confirmDirect/{requestId}")
    public ResponseEntity<String> confirmDirectExchange(@PathVariable Long requestId) {
        try {
            return ResponseEntity.ok(exchangeService.confirmDirectExchange(requestId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // next step agree/confirm endpoints for direct and public requests, then go verify
    // note that internal service logic should update workshift service on agreement 


}
