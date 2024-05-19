package cz.cvut.fel.perfomanceservice.dto;

import cz.cvut.fel.perfomanceservice.model.Performance;
import cz.cvut.fel.workshiftservice.model.WorkShift;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceDTO {
    private Performance performance;
    private List<WorkShift> workShifts;
}