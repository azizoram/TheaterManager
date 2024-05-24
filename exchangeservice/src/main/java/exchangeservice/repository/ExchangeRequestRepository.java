package exchangeservice.repository;

import exchangeservice.model.ExchangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Long> {

    List<ExchangeRequest> findAllByAuthorId(Long authorId);
}
