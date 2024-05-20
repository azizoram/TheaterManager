package performanceservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @ElementCollection
    @CollectionTable(name = "performance_shifts", joinColumns = @JoinColumn(name = "performance_id"))
    @Column(name = "shift_ids")
    private List<Long> workShiftsIds = new ArrayList<>();

//    public List<Long> getShiftIds() {
//        return workShiftsIds;
//    }
}
