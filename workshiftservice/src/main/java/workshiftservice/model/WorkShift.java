package workshiftservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "shift")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WorkShift {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "shift_id")
    private Long id;
    private String name;
    private String description;
    private LocalDateTime start;
    private LocalDateTime endTime;
    private int capacity;

    @Column(name = "performance_id")
    private Long performanceId;

    @ElementCollection
    @CollectionTable(name = "workshift_roles", joinColumns = @JoinColumn(name = "shift_id"))
    @MapKeyColumn(name = "role")
    @Column(name = "quantity")
    private Map<String, Integer> requiredRoles;

    @ElementCollection
    @CollectionTable(name = "workshift_employees", joinColumns = @JoinColumn(name = "shift_id"))
    @Column(name = "capacity")
    private Map<Long, String> employeesCapacity;

    public Map<Long, String> getEmployees() {
        return employeesCapacity == null ? new HashMap<>() : employeesCapacity;
    }
}
