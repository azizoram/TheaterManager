package performanceservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceRequest {
    private String name;
    private String description;
    private Integer duration;
    private Long[] workShiftsIds;
}
