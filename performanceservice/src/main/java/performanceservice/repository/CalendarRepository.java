package performanceservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import performanceservice.model.Calendar;

import java.time.YearMonth;
import java.util.Optional;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    Optional<Calendar> findByMonth(YearMonth month);

}
