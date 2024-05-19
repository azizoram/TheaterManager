package cz.cvut.fel.perfomanceservice.controller;

import cz.cvut.fel.perfomanceservice.dto.PerformanceDTO;
import cz.cvut.fel.perfomanceservice.model.Performance;
import cz.cvut.fel.perfomanceservice.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/performances")
public class PerformanceController {
    private final PerformanceService performanceService;

    @Autowired
    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PerformanceDTO> getPerformanceWithShifts(@PathVariable Long id) {
        PerformanceDTO performanceDTO = performanceService.getPerformanceWithShifts(id);
        return ResponseEntity.ok(performanceDTO);
    }

    @PostMapping
    public ResponseEntity<Performance> createPerformance(@RequestBody Performance performance) {
        return new ResponseEntity<>(performanceService.createPerformance(performance), HttpStatus.CREATED);
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
}
