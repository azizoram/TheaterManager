package performanceservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import performanceservice.model.Performance;
import performanceservice.service.CalendarService;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {
    @Autowired
    private CalendarService calendarService;

    @PostMapping("/{year}/{month}/addPerformance/{performanceId}")
    public ResponseEntity<Void> addPerformanceToMonth(
            @PathVariable int year,
            @PathVariable int month,

            @PathVariable Long performanceId) {
        YearMonth yearMonth = YearMonth.of(year, month);
        calendarService.addPerformanceToCalendarById(yearMonth, performanceId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{year}/{month}/performances")
    public ResponseEntity<List<Performance>> getPerformancesForMonth(
            @PathVariable int year,
            @PathVariable int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        List<Performance> performances = calendarService.getPerformancesForMonth(yearMonth);
        return ResponseEntity.ok(performances);
    }

    public ResponseEntity<Void> removePerformanceFromMonth(
            @PathVariable int year,
            @PathVariable int month,
            @PathVariable Long performanceId) {
        YearMonth yearMonth = YearMonth.of(year, month);
        calendarService.removePerformanceFromCalendar(yearMonth, performanceId);
        return ResponseEntity.ok().build();
    }

}