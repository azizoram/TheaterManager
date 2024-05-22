package userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceDTO {
    private Long id;
    private String name;
    private String description;
    private Integer duration;
    private LocalDateTime start;
    private LocalDateTime endTime;

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PerformanceDTO performanceDTO = (PerformanceDTO) obj;
        return id.equals(performanceDTO.id);
    }
}
