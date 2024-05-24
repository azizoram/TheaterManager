package workshiftservice.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import workshiftservice.dto.EmployeesResponse;
import workshiftservice.dto.ShiftChangeRequestDTO;
import workshiftservice.dto.WorkShiftDTO;
import workshiftservice.exception.NoSuchShiftException;
import workshiftservice.model.WorkShift;
import workshiftservice.repository.WorkShiftRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WorkShiftService {

    private final WorkShiftRepository workShiftRepository;

//    private final WebClient webClient; odnazdy ja tak rygnul azh sam v ahuii byl
    private final WebClient.Builder webClientBuilder;

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

    public void addEmployee(Long id, Long employeeId, String role) {
        WorkShift workShift = workShiftRepository.findById(id).orElseThrow();
        if (workShift.getEmployees().containsKey(employeeId)) {
            throw new RuntimeException("Employee already in shift");
        }
        workShift.getEmployees().put(employeeId, role);
        workShiftRepository.save(workShift);
    }

    public void removeEmployee(Long id, Long employeeId) {
        WorkShift workShift = workShiftRepository.findById(id).orElseThrow();
        if (!workShift.getEmployees().containsKey(employeeId)) {
            throw new RuntimeException("Employee not in shift");
        }
        workShift.getEmployees().remove(employeeId);
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

        List<Long> employeesIdList = workShiftRepository.findById(id).orElseThrow().getEmployees().keySet().stream().toList();
//        String employeesJson = objectMapper.writeValueAsString(employeesIdList);
        List<EmployeesResponse> employeesResponses = webClientBuilder.build()
                .post()
                .uri("http://userservice/api/user/employees")
                .bodyValue(employeesIdList)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<EmployeesResponse>>() {})
                .block();

        return employeesResponses.stream().map(employeesResponse -> {
            employeesResponse.setRole(workShiftRepository.findById(id).orElseThrow().getEmployees().get(employeesResponse.getId()));
            return employeesResponse;
        }).toList();

    }

    public List<WorkShiftDTO> getShiftsByEmployee(Long employeeId) {
        return workShiftRepository.findAll().stream()
                .filter(workShift -> workShift.getEmployees().containsKey(employeeId))
                .map(this::mapToWorkShiftDTO)
                .toList();
    }

    public String verifyChangeShiftRequest(ShiftChangeRequestDTO workShiftDTO) {
        Objects.requireNonNull(workShiftDTO.getShiftId());
        Optional<WorkShift> workShift = workShiftRepository.findById(workShiftDTO.getShiftId());
        if (workShift.isEmpty()) {
            return "Shift not found";
        }
        Objects.requireNonNull(workShiftDTO.getAuthorId());
        Long employeeId = workShiftDTO.getAuthorId();
        if (!workShift.get().getEmployees().containsKey(employeeId)) {
            return "Employee not in shift";
        }

        Long askedEmployeeId = workShiftDTO.getConsumer();
        if (workShift.get().getEmployees().containsKey(askedEmployeeId)) {
            return "Employee is already on shift";
        }

        String requestedRole = workShiftDTO.getRole();
        Objects.requireNonNull(requestedRole);
        String role = workShift.get().getEmployees().get(employeeId);

        return requestedRole.equals(role) ? "OK" : "Requested role does not match employee role";

    }
}
