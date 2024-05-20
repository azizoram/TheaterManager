package performanceservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShiftResponse {

    private String name;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;
    private int capacity;
    private Map<String, Integer> requiredRoles;
    private List<String> employees;
}
