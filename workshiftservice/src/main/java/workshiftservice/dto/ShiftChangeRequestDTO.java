package workshiftservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShiftChangeRequestDTO {
        private Long shiftId;
        private Long authorId;
        private String role;
        private Long consumer;
}
