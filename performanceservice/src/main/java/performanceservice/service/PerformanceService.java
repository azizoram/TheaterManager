package performanceservice.service;

import performanceservice.dto.PerformanceDTO;
import performanceservice.dto.PerformanceRequest;
import performanceservice.exception.ResourceNotFoundException;
import performanceservice.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import performanceservice.model.WorkShift;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import performanceservice.model.Performance;
import workshiftservice.dto.WorkShiftResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PerformanceService {
    private final WebClient webClient;
    private final PerformanceRepository performanceRepository;

//    @Autowired
//    public PerformanceService(WebClient webClient, PerformanceRepository performanceRepository) {
//        this.webClient = webClient;
//        this.performanceRepository = performanceRepository;
//    }

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

    public List<WorkShiftResponse> getShiftsByPerformanceId(Long performanceId) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Performance not found"));

        return performance.getWorkShiftsIds().stream()
                .map(shiftId -> webClient
                        .get()
                        .uri("http://localhost:8081/api/workshift/" + shiftId)
                        .retrieve()
                        .bodyToMono(WorkShiftResponse.class)
                        .block())
                .collect(Collectors.toList());
    }
    private WorkShiftResponse mapToWorkShiftResponse(workshiftservice.model.WorkShift workShift) {
        return WorkShiftResponse.builder()
                .name(workShift.getName())
                .description(workShift.getDescription())
                .start(workShift.getStart())
                .end(workShift.getEndTime())
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
        Performance performance = Performance.builder()
        .name(performanceRequest.getName())
        .description(performanceRequest.getDescription())
        .duration(performanceRequest.getDuration())
                .build();
        performanceRepository.save(performance);
    }

    public Performance addShiftToPerformance(Long performanceId, Long shiftId) {
        Performance performance = getPerformance(performanceId);
        performance.getWorkShiftsIds().add(shiftId);
        return performanceRepository.save(performance);
    }

    public Performance removeShiftFromPerformance(Long performanceId, Long shiftId) {
        Performance performance = getPerformance(performanceId);
        performance.getWorkShiftsIds().remove(shiftId);
        return performanceRepository.save(performance);
    }
}
