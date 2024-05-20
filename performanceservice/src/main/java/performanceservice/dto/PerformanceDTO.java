package performanceservice.dto;

import performanceservice.model.Performance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import performanceservice.model.WorkShift;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceDTO {
    private Performance performance;
    private List<WorkShift> workShifts;
}