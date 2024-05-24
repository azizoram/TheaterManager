package exchangeservice.dto;

import exchangeservice.model.ExchangeDirect;
import exchangeservice.model.ExchangeRequest;
import lombok.Builder;
import lombok.Data;

@Data
public class ShiftRequestDTO {
    private Long id;
    private Long shiftId;
    private Long authorId;
    private String role;
    private Long consumer;

    public ShiftRequestDTO(ExchangeDirect exchangeDirect) {
        this.id = exchangeDirect.getId();
        this.shiftId = exchangeDirect.getShiftId();
        this.authorId = exchangeDirect.getAuthorId();
        this.role = exchangeDirect.getRole();
        this.consumer = exchangeDirect.getConsumer();
    }

    public ShiftRequestDTO(ExchangeRequest exchangeRequest){
        this.shiftId = exchangeRequest.getShiftId();
        this.authorId = exchangeRequest.getAuthorId();
        this.role = exchangeRequest.getRole();
        this.id = exchangeRequest.getId();
    }

    public ShiftRequestDTO(Object o) {
    }
}
