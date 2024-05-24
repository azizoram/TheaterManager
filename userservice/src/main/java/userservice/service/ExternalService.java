package userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import userservice.dto.PerformanceDTO;
import userservice.dto.UserDTO;
import userservice.dto.WorkshiftDTO;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@Lazy
public class ExternalService {

    private static final String workshift_api = "http://workshift/api/";
    private static final String performance_api = "http://performances//api/";

    private final WebClient.Builder webClient;

    public List<WorkshiftDTO> getWorkShifts(Set<Long> workShifts) {
        return workShifts.stream()
                .map(shiftId -> webClient.build()
                        .get()
                        .uri(workshift_api + shiftId)
                        .retrieve()
                        .bodyToMono(WorkshiftDTO.class)
                        .block())
                .toList();
    }


    public List<PerformanceDTO> getPerformances(Set<Long> workShifts) {
        return workShifts.stream()
                .map(shiftId -> webClient.build()
                        .get()
                        .uri(workshift_api + shiftId)
                        .retrieve()
                        .bodyToMono(WorkshiftDTO.class)
                        .block()).filter(Objects::nonNull)
                .map(WorkshiftDTO::getPerformanceId).distinct().map(
                        performanceId -> webClient.build()
                                .get()
                                .uri(performance_api + performanceId)
                                .retrieve()
                                .bodyToMono(PerformanceDTO.class)
                                .block()).toList();

    }

    public List<UserDTO> getAllEmployeeFromShift(Long id) {
        return webClient.build()
                .get()
                .uri(workshift_api + id + "/employees")
                .retrieve()
                .bodyToFlux(UserDTO.class)
                .collectList()
                .block();
    }

}
