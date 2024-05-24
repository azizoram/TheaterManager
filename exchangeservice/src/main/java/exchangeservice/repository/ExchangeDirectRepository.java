package exchangeservice.repository;

import exchangeservice.model.ExchangeDirect;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ExchangeDirectRepository extends JpaRepository<ExchangeDirect, Long> {
}
