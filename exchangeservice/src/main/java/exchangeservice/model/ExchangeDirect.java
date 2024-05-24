package exchangeservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exchange_response")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ExchangeDirect {
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

    @Column(name = "from_user_id", nullable = false)
    private Long consumer;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ExchangeRequest> exchangeRequests = new ArrayList<>();

    public ExchangeDirect(Long shiftid, Long authorId, String role, Long fromExchange) {
        this.shiftId = shiftid;
        this.authorId = authorId;
        this.role = role;
        this.consumer = fromExchange;
    }
}
