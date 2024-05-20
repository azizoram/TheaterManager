package performanceservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import performanceservice.dto.PerformanceRequest;
import performanceservice.model.Calendar;
import performanceservice.model.Performance;
import performanceservice.repository.CalendarRepository;
import performanceservice.repository.PerformanceRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CalendarService {
    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private PerformanceRepository performanceRepository;

    public Calendar getOrCreateCalendar(YearMonth month) {
        return calendarRepository.findByMonth(month).orElseGet(() -> {
            Calendar calendar = Calendar.builder().month(month).build();
            return calendarRepository.save(calendar);
        });
    }

    public void addPerformanceToCalendar(YearMonth month, Performance performance) {
        Calendar calendar = getOrCreateCalendar(month);
//        if (calendar.getPerformances() == null) {
//            calendar.setPerformances(new ArrayList<>());
//        }
        calendar.getPerformances().add(performance);
        calendarRepository.save(calendar);
    }

    public void addPerformanceToCalendarById(YearMonth month, Long performanceId) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new EntityNotFoundException("Performance not found"));

        Calendar calendar = getOrCreateCalendar(month);
        List<Performance> performances = calendar.getPerformances();
        if (calendar.getPerformances().contains(performance)) {
            throw new IllegalArgumentException("Performance is already in the calendar");
        }

        boolean isWithinMonth = performance.getDates().entrySet().stream()
                .anyMatch(entry -> {
                    LocalDateTime startDateTime = entry.getKey();
                    LocalDateTime endDateTime = entry.getValue();
                    LocalDate startDate = startDateTime.toLocalDate();
                    LocalDate endDate = endDateTime.toLocalDate();

                    // Check if the performance date range overlaps with the specified month
                    return (startDate.isBefore(month.atEndOfMonth().plusDays(1)) && endDate.isAfter(month.atDay(1).minusDays(1)));
                });

        if (!isWithinMonth) {
            throw new IllegalArgumentException("Performance dates are out of the specified month");
        }
        addPerformanceToCalendar(month, performance);
    }



    public List<Performance> getPerformancesForMonth(YearMonth month) {
        return calendarRepository.findByMonth(month)
                .map(Calendar::getPerformances)
                .orElse(List.of());
    }

    public void deletePerformanceFromCalendar(YearMonth month, Long performanceId) {
        Calendar calendar = getOrCreateCalendar(month);
        Performance performance = performanceRepository.findById(performanceId).orElseThrow();
        calendar.getPerformances().remove(performance);
        calendarRepository.save(calendar);
    }

    public void removePerformanceFromCalendar(YearMonth yearMonth, Long performanceId) {
        Calendar calendar = getOrCreateCalendar(yearMonth);
        Performance performance = performanceRepository.findById(performanceId).orElseThrow();
        calendar.getPerformances().remove(performance);
        System.out.printf("Performance %s was removed from calendar%n", performance.getName());
        calendarRepository.save(calendar);
    }

    public void updatePerformanceInCalendar(YearMonth yearMonth, Long performanceId, PerformanceRequest performanceRequest) {
        Calendar calendar = getOrCreateCalendar(yearMonth);
        Performance performance = performanceRepository.findById(performanceId).orElseThrow();
        performance.setName(performanceRequest.getName());
        performance.setDescription(performanceRequest.getDescription());
        performance.setDuration(performanceRequest.getDuration());
        Map<LocalDateTime, LocalDateTime> dates = Map.of(performanceRequest.getStart(), performanceRequest.getEndTime());
        performance.setDates(dates);
        calendarRepository.save(calendar);
    }
}
