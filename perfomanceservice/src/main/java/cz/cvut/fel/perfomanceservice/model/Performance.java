package cz.cvut.fel.perfomanceservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "perfomance")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "perfomance_id")
    private Long id;
    private String name;
    private String description;
    private Integer duration;

    @ElementCollection
    @CollectionTable(name = "perfomance_workshifts", joinColumns = @JoinColumn(name = "perfomance_id"))
    @Column(name = "workshift_id")
    private List<Integer> workShiftsIds = new ArrayList<>();

    public List<Long> getShiftIds() {
        return workShiftsIds.stream()
                .map(Integer::longValue)
                .toList();
    }
}
