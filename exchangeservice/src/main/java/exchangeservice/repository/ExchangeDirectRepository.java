package exchangeservice.repository;

import exchangeservice.model.ExchangeDirect;
import exchangeservice.model.ExchangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ExchangeDirectRepository extends JpaRepository<ExchangeDirect, Long> {
    List<ExchangeDirect> findAllByConsumer(Long consumerId);

}
