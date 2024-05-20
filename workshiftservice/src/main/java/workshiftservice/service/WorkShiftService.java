package workshiftservice.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import workshiftservice.dto.EmployeesResponse;
import workshiftservice.dto.WorkShiftRequest;
import workshiftservice.dto.WorkShiftResponse;
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
public class WorkShiftService {

    private final WorkShiftRepository workShiftRepository;

    private final WebClient webClient;

    public void createWorkShift(WorkShiftRequest workShiftRequest) {
        WorkShift workShift = WorkShift.builder()
                .name(workShiftRequest.getName())
                .description(workShiftRequest.getDescription())
                .start(workShiftRequest.getStart())
                .endTime(workShiftRequest.getEndTime())
                .capacity(workShiftRequest.getCapacity())
                .build();
        workShiftRepository.save(workShift);
    }

    public List<WorkShiftResponse> getAllWorkShifts() {
        List<WorkShift> shifts = workShiftRepository.findAll();
        return shifts.stream()
                .map(this::mapToWorkShiftResponse)
                .toList();
    }

    private WorkShiftResponse mapToWorkShiftResponse(WorkShift workShift) {
        return WorkShiftResponse.builder()
                .name(workShift.getName())
                .description(workShift.getDescription())
                .start(workShift.getStart())
                .endTime(workShift.getEndTime())
                .capacity(workShift.getCapacity())
                .requiredRoles(workShift.getRequiredRoles())
                .employees(workShift.getEmployees())
                .build();
    }

    public WorkShiftResponse getWorkShift(Long id) {
        return workShiftRepository.findById(id)
                .map(this::mapToWorkShiftResponse)
                .orElseThrow(() -> new NoSuchShiftException("Shift not found"));
    }

    public void deleteWorkShift(Long id) {
        workShiftRepository.deleteById(id);
    }


    public void updateWorkShift(Long id, WorkShiftRequest workShiftRequest) {
        WorkShift workShift = workShiftRepository.findById(id).orElseThrow();
        workShift.setName(workShiftRequest.getName());
        workShift.setDescription(workShiftRequest.getDescription());
        workShift.setStart(workShiftRequest.getStart());
        workShift.setEndTime(workShiftRequest.getEndTime());
        workShift.setCapacity(workShiftRequest.getCapacity());
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

    public List<WorkShiftResponse> getShiftsByEmployee(String email) {
        return workShiftRepository.findAll().stream()
                .filter(workShift -> workShift.getEmployees().contains(email))
                .map(this::mapToWorkShiftResponse)
                .toList();
    }

}
