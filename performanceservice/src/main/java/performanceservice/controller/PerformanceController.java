package performanceservice.controller;

import performanceservice.dto.PerformanceDTO;
import performanceservice.dto.PerformanceRequest;
import performanceservice.dto.WorkShiftDTO;
import performanceservice.model.Performance;
import performanceservice.service.PerformanceService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/performances")
@RequiredArgsConstructor
public class PerformanceController {
    private final PerformanceService performanceService;

    @GetMapping("/with-shifts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PerformanceDTO> getPerformanceWithShifts(@PathVariable Long id) {
        PerformanceDTO performanceDTO = performanceService.getPerformanceWithShifts(id);
        return ResponseEntity.ok(performanceDTO);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createPerformance(@RequestBody PerformanceRequest performance) {
        performanceService.createPerformance(performance);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Performance> getPerformance(@PathVariable Long id) {
        return ResponseEntity.ok(performanceService.getPerformance(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Performance> updatePerformance(@PathVariable Long id, @RequestBody Performance performanceDetails) {
        return ResponseEntity.ok(performanceService.updatePerformance(id, performanceDetails));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePerformance(@PathVariable Long id) {
        performanceService.deletePerformance(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Performance>> getAllPerformances() {
        return ResponseEntity.ok(performanceService.getAllPerformances());
    }

    @PostMapping("/{performanceId}/shifts/{shiftId}")
    public ResponseEntity<Performance> addShiftToPerformance(@PathVariable Long performanceId, @PathVariable Long shiftId) {
        return ResponseEntity.ok(performanceService.addShiftToPerformance(performanceId, shiftId));
    }

    @DeleteMapping("/{performanceId}/shifts/{shiftId}")
    public ResponseEntity<Performance> removeShiftFromPerformance(@PathVariable Long performanceId, @PathVariable Long shiftId) {
        return ResponseEntity.ok(performanceService.removeShiftFromPerformance(performanceId, shiftId));
    }

    @GetMapping("/shifts/{performanceId}")
    public List<WorkShiftDTO> getShiftsByPerformanceId(@PathVariable Long performanceId) {
        return performanceService.getShiftsByPerformanceId(performanceId);
    }
}
