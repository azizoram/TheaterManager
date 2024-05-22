package workshiftservice.dto;

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
public class WorkShiftDTO {
    private Long id;
    private Long performanceId;
    private String name;
    private String description;
    private LocalDateTime start;
    private LocalDateTime endTime;
    private int capacity;
    private Map<String, Integer> requiredRoles;
    private Map<Long, String> employees;
}
