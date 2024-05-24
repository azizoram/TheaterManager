package performanceservice.dto;

import ch.qos.logback.core.joran.sanity.Pair;
import jakarta.persistence.*;
import performanceservice.model.Performance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import performanceservice.model.WorkShift;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceDTO {

    private Long id;
    private String name;
    private String description;
    private Integer duration;

    private List<Long> workShiftsIds = new ArrayList<>();

    private List<DateIntervalDTO> dates;

    public PerformanceDTO(Performance performance) {
        this.id = performance.getId();
        this.name = performance.getName();
        this.description = performance.getDescription();
        this.duration = performance.getDuration();
        this.workShiftsIds = performance.getWorkShiftsIds();
        this.dates = new ArrayList<>(new ArrayList<>(performance.getDates().entrySet().stream().map(e -> new DateIntervalDTO(e.getKey().toString(), e.getValue().toString())).collect(Collectors.toList())));
    }

    public PerformanceDTO(Performance performance, List<WorkShift> workShifts) {
        this.id = performance.getId();
        this.name = performance.getName();
        this.description = performance.getDescription();
        this.duration = performance.getDuration();
        this.workShiftsIds = workShifts.stream().map(WorkShift::getId).collect(Collectors.toList());
        this.dates = new ArrayList<>(new ArrayList<>(performance.getDates().entrySet().stream().map(e -> new DateIntervalDTO(e.getKey().toString(), e.getValue().toString())).collect(Collectors.toList())));
    }
}