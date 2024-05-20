package performanceservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import java.util.*;

@Entity
@Table(name = "calendar")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "calendar_id")
    private Long id;

    private YearMonth month;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "calendar_id")
    private List<Performance> performances = new ArrayList<>();

    public List<Performance> getPerformances() {
        return performances == null ? new ArrayList<>() : performances;
    }

}
