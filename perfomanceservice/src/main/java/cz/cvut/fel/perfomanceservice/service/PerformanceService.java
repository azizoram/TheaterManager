package cz.cvut.fel.perfomanceservice.service;

import cz.cvut.fel.perfomanceservice.dto.PerformanceDTO;
import cz.cvut.fel.perfomanceservice.exception.ResourceNotFoundException;
import cz.cvut.fel.perfomanceservice.repository.PerformanceRepository;
import cz.cvut.fel.workshiftservice.model.WorkShift;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import cz.cvut.fel.perfomanceservice.model.Performance;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PerformanceService {
    private final WebClient webClient;
    private final PerformanceRepository performanceRepository;

    @Autowired
    public PerformanceService(WebClient webClient, PerformanceRepository performanceRepository) {
        this.webClient = webClient;
        this.performanceRepository = performanceRepository;
    }

    public PerformanceDTO getPerformanceWithShifts(Long performanceId) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new RuntimeException("Performance not found"));

        List<Long> shiftIds = performance.getShiftIds();
        List<WorkShift> workShifts = shiftIds.stream()
                .map(shiftId -> webClient
                        .get()
                        .uri("http://api/performances/" + shiftId)
                        .retrieve()
                        .bodyToMono(WorkShift.class)
                        .block())
                .collect(Collectors.toList());

        return new PerformanceDTO(performance, workShifts);
    }

    public Performance getPerformance(Long id) {
        return performanceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Performance not found"));
    }

    public Performance updatePerformance(Long id, Performance performanceDetails) {
        Performance performance = getPerformance(id);
        performance.setName(performanceDetails.getName());
        performance.setDescription(performanceDetails.getDescription());
        performance.setDuration(performanceDetails.getDuration());
        return performanceRepository.save(performance);
    }

    public void deletePerformance(Long id) {
        Performance performance = getPerformance(id);
        performanceRepository.delete(performance);
    }

    public List<Performance> getAllPerformances() {
        return performanceRepository.findAll();
    }

    public Performance createPerformance(Performance performance) {
        return performanceRepository.save(performance);
    }

    public Performance addShiftToPerformance(Long performanceId, Long shiftId) {
        Performance performance = getPerformance(performanceId);
        performance.getShiftIds().add(shiftId);
        return performanceRepository.save(performance);
    }

    public Performance removeShiftFromPerformance(Long performanceId, Long shiftId) {
        Performance performance = getPerformance(performanceId);
        performance.getShiftIds().remove(shiftId);
        return performanceRepository.save(performance);
    }
}
