package performanceservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "performance")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "performance_id")
    private Long id;
    private String name;
    private String description;
    private Integer duration;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "performance_shifts", joinColumns = @JoinColumn(name = "performance_id"))
    @Column(name = "shift_ids")
    private List<Long> workShiftsIds = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "performance_dates", joinColumns = @JoinColumn(name = "performance_id"))
    @Column(name = "dates")
    private Map<LocalDateTime, LocalDateTime> dates;

@Override
    public String toString(){
        return "Performance{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                ", workShiftsIds=" + workShiftsIds +
                ", dates=" + dates +
                '}';
    }
}
