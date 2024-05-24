package exchangeservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exchange_request")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ExchangeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @Column(name = "shift_id", nullable = false)
    private Long shiftId;

    @Column(name = "role", nullable = false)
    private String role;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "request_response",
            joinColumns = @JoinColumn(name = "exchange_request_id"),
            inverseJoinColumns = @JoinColumn(name = "exchange_response_id")
    )
    private List<ExchangeDirect> responses = new ArrayList<>();

    public ExchangeRequest(Long shiftid, Long authorId, String role) {
        this.shiftId = shiftid;
        this.authorId = authorId;
        this.role = role;
    }
}
