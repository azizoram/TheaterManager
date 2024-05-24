package exchangeservice.repository;

import exchangeservice.model.ExchangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Long> {
    Collection<Object> findAllByConsumer(Long consumerId);

    Collection<Object> findAllByAuthorId(Long authorId);
}
