package cz.cvut.fel.workshiftservice.service;

import cz.cvut.fel.workshiftservice.dto.WorkShiftRequest;
import cz.cvut.fel.workshiftservice.dto.WorkShiftResponse;
import cz.cvut.fel.workshiftservice.model.WorkShift;
import cz.cvut.fel.workshiftservice.repository.WorkShiftRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkShiftService {

    private final WorkShiftRepository workShiftRepository;

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
                .end(workShift.getEndTime())
                .capacity(workShift.getCapacity())
                .requiredRoles(workShift.getRequiredRoles())
                .employees(workShift.getEmployees())
                .build();
    }

    public WorkShiftResponse getWorkShift(Long id) {
        return workShiftRepository.findById(id)
                .map(this::mapToWorkShiftResponse)
                .orElseThrow();
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
        workShift.getEmployees().add(email);
        workShiftRepository.save(workShift);
    }

    public void removeEmployee(Long id, String email) {
        WorkShift workShift = workShiftRepository.findById(id).orElseThrow();
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

    public void exchangeShifts(Long id1, Long id2, String email) {
    }


}
