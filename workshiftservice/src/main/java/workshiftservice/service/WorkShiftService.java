package workshiftservice.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import workshiftservice.dto.EmployeesResponse;
import workshiftservice.dto.WorkShiftDTO;
import workshiftservice.dto.WorkShiftDTO;
import workshiftservice.exception.NoSuchShiftException;
import workshiftservice.model.WorkShift;
import workshiftservice.repository.WorkShiftRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WorkShiftService {

    private final WorkShiftRepository workShiftRepository;

    private final WebClient webClient;

    public void createWorkShift(WorkShiftDTO WorkShiftDTO) {
        WorkShift workShift = WorkShift.builder()
                .performanceId(WorkShiftDTO.getPerformanceId())
                .name(WorkShiftDTO.getName())
                .description(WorkShiftDTO.getDescription())
                .start(WorkShiftDTO.getStart())
                .endTime(WorkShiftDTO.getEndTime())
                .capacity(WorkShiftDTO.getCapacity())
                .build();
        workShiftRepository.save(workShift);
    }

    public List<WorkShiftDTO> getAllWorkShifts() {
        List<WorkShift> shifts = workShiftRepository.findAll();
        return shifts.stream()
                .map(this::mapToWorkShiftDTO)
                .toList();
    }

    private WorkShiftDTO mapToWorkShiftDTO(WorkShift workShift) {
        return WorkShiftDTO.builder()
                .id(workShift.getId())
                .performanceId(workShift.getPerformanceId())
                .name(workShift.getName())
                .description(workShift.getDescription())
                .start(workShift.getStart())
                .endTime(workShift.getEndTime())
                .capacity(workShift.getCapacity())
                .requiredRoles(workShift.getRequiredRoles())
                .employees(workShift.getEmployees())
                .build();
    }

    public WorkShiftDTO getWorkShift(Long id) {
        return workShiftRepository.findById(id)
                .map(this::mapToWorkShiftDTO)
                .orElseThrow(() -> new NoSuchShiftException("Shift not found"));
    }

    public void deleteWorkShift(Long id) {
        workShiftRepository.deleteById(id);
    }


    public void updateWorkShift(Long id, WorkShiftDTO WorkShiftDTO) {
        WorkShift workShift = workShiftRepository.findById(id).orElseThrow();
        workShift.setName(WorkShiftDTO.getName());
        workShift.setDescription(WorkShiftDTO.getDescription());
        workShift.setStart(WorkShiftDTO.getStart());
        workShift.setEndTime(WorkShiftDTO.getEndTime());
        workShift.setCapacity(WorkShiftDTO.getCapacity());
        workShiftRepository.save(workShift);
    }

    public void addEmployee(Long id, String email) {
        WorkShift workShift = workShiftRepository.findById(id).orElseThrow();
        if (workShift.getEmployees().contains(email)) {
            throw new RuntimeException("Employee already in shift");
        }
        workShift.getEmployees().add(email);
        workShiftRepository.save(workShift);
    }

    public void removeEmployee(Long id, String email) {
        WorkShift workShift = workShiftRepository.findById(id).orElseThrow();
        if (!workShift.getEmployees().contains(email)) {
            throw new RuntimeException("Employee not in shift");
        }
        workShift.getEmployees().remove(email);
        workShiftRepository.save(workShift);
    }

    public void addRole(Long id, Map<String, Integer> role) {
        WorkShift workShift = workShiftRepository.findById(id).orElseThrow();
        Map<String, Integer> requiredRoles = workShift.getRequiredRoles();
        requiredRoles.putAll(role);
        workShiftRepository.save(workShift);
    }

    public void removeRole(Long id, String role) {
        WorkShift workShift = workShiftRepository.findById(id).orElseThrow();
        workShift.getRequiredRoles().remove(role);
        workShiftRepository.save(workShift);
    }

    public void updateRoleQuantity(Long id, String role, int quantity) {
        WorkShift workShift = workShiftRepository.findById(id).orElseThrow();
        workShift.getRequiredRoles().put(role, quantity);
        workShiftRepository.save(workShift);
    }

    public void updateCapacity(Long id, int capacity) {
        WorkShift workShift = workShiftRepository.findById(id).orElseThrow();
        workShift.setCapacity(capacity);
        workShiftRepository.save(workShift);
    }

    public String getWorkShiftInfo(Long id) {
        WorkShift workShift = workShiftRepository.findById(id).orElseThrow();
        return workShift.toString();
    }

    public List<EmployeesResponse> getAllEmployeeFromShift(Long id) {
        WorkShift workShift = workShiftRepository.findById(id).orElseThrow();
        return workShift.getEmployees().stream().map(employee -> webClient
                .get()
                .uri("http://localhost:8082/api/employee/" + employee)
                .retrieve()
                .bodyToMono(EmployeesResponse.class)
                .block())
                .collect(toList());
    }

    public List<WorkShiftDTO> getShiftsByEmployee(String email) {
        return workShiftRepository.findAll().stream()
                .filter(workShift -> workShift.getEmployees().contains(email))
                .map(this::mapToWorkShiftDTO)
                .toList();
    }

}
