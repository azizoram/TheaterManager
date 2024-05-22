package performanceservice.service;

import performanceservice.dto.PerformanceDTO;
import performanceservice.dto.PerformanceRequest;
import performanceservice.dto.WorkShiftDTO;
import performanceservice.exception.ResourceNotFoundException;
import performanceservice.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import performanceservice.model.WorkShift;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import performanceservice.model.Performance;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PerformanceService {
    private final WebClient webClient;
    private final PerformanceRepository performanceRepository;

    // Mb useless method, because we can get shifts from performance
    public PerformanceDTO getPerformanceWithShifts(Long performanceId) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new RuntimeException("Performance not found"));

        List<Long> shiftIds = performance.getWorkShiftsIds();
        List<WorkShift> workShifts = shiftIds.stream()
                .map(shiftId -> webClient
                        .get()
                        .uri("http://localhost:8081/api/workshift/" + shiftId)
                        .retrieve()
                        .bodyToMono(WorkShift.class)
                        .block())
                .collect(Collectors.toList());

        return new PerformanceDTO(performance, workShifts);
    }

    public List<WorkShiftDTO> getShiftsByPerformanceId(Long performanceId) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Performance not found"));

        return performance.getWorkShiftsIds().stream()
                .map(shiftId -> webClient
                        .get()
                        .uri("http://localhost:8081/api/workshift/" + shiftId)
                        .retrieve()
                        .bodyToMono(WorkShiftDTO.class)
                        .block())
                .collect(Collectors.toList());
    }
    private WorkShiftDTO mapToWorkShiftDTO(WorkShift workShift) {
        return WorkShiftDTO.builder()
                .name(workShift.getName())
                .description(workShift.getDescription())
                .start(workShift.getStart())
                .endTime(workShift.getEndTime())
                .capacity(workShift.getCapacity())
                .requiredRoles(workShift.getRequiredRoles())
                .employees(workShift.getEmployees())
                .build();
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

    public void createPerformance(PerformanceRequest performanceRequest) {
        if (performanceRequest.getStart().isAfter(performanceRequest.getEndTime())) {
            throw new RuntimeException("Start date is after end date");
        }
        Map<LocalDateTime, LocalDateTime> dates = Map.of(performanceRequest.getStart(), performanceRequest.getEndTime());
        Performance performance = Performance.builder()
        .name(performanceRequest.getName())
        .description(performanceRequest.getDescription())
        .duration(performanceRequest.getDuration())
                .dates(dates)
                .build();
        performanceRepository.save(performance);
    }

    public Performance addShiftToPerformance(Long performanceId, Long shiftId) {
        Performance performance = getPerformance(performanceId);
        WorkShiftDTO shift = webClient
                .get()
                .uri("http://localhost:8081/api/workshift/" + shiftId)
                .retrieve()
                .bodyToMono(WorkShiftDTO.class)
                .block();
        System.out.println(shift.toString());
        if (shift == null) {
            throw new ResourceNotFoundException("Shift not found");
        }
        else if (performance.getWorkShiftsIds().contains(shiftId)) {
            throw new RuntimeException("Shift already added to performance");
        }
        else if (shift.getStart().isBefore(performance.getDates().keySet().stream().findFirst().get()) ||
                shift.getEndTime().isAfter(performance.getDates().values().stream().findFirst().get())) {
            throw new RuntimeException("Shift is not in performance dates");
        }
        performance.getWorkShiftsIds().add(shiftId);
        return performanceRepository.save(performance);
    }

    public Performance removeShiftFromPerformance(Long performanceId, Long shiftId) {
        Performance performance = getPerformance(performanceId);
        performance.getWorkShiftsIds().remove(shiftId);
        return performanceRepository.save(performance);
    }

    public String getPerformanceName(Long performanceId) {
        return performanceRepository.findById(performanceId).orElseThrow(() -> new ResourceNotFoundException("Performance not found")).getName();
    }

    public String getInfoAboutPerformance(Long performanceId) {
        Performance performance = performanceRepository.findById(performanceId).orElseThrow(() -> new ResourceNotFoundException("Performance not found"));
        return "Performance name: " + performance.getName() + "\n" +
                "Performance description: " + performance.getDescription() + "\n" +
                "Performance duration: " + performance.getDuration() + "\n" +
                "Performance dates: " + performance.getDates();
    }
}
