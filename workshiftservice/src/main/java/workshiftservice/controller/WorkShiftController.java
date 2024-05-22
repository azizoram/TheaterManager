package workshiftservice.controller;

import org.springframework.http.ResponseEntity;
import workshiftservice.dto.EmployeesResponse;
import workshiftservice.dto.WorkShiftDTO;
import workshiftservice.dto.WorkShiftDTO;

import workshiftservice.service.WorkShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workshift")
@RequiredArgsConstructor
public class WorkShiftController {

    private final WorkShiftService workShiftService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createWorkShift(@RequestBody WorkShiftDTO WorkShiftDTO) {
        workShiftService.createWorkShift(WorkShiftDTO);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<WorkShiftDTO> getAllWorkShifts() {
        return workShiftService.getAllWorkShifts();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public WorkShiftDTO getWorkShift(@PathVariable Long id) {
        return workShiftService.getWorkShift(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorkShift(@PathVariable Long id) {
        workShiftService.deleteWorkShift(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateWorkShift(@PathVariable Long id, @RequestBody WorkShiftDTO WorkShiftDTO) {
        workShiftService.updateWorkShift(id, WorkShiftDTO);
    }

    @PostMapping("/{shifId}/{employeeId}/{role}")
    @ResponseStatus(HttpStatus.OK)
    public void addEmployeeToWorkShift(@PathVariable Long shifId, @PathVariable Long employeeId, @PathVariable String role) {
        workShiftService.addEmployee(shifId, employeeId, role);
    }

    @PostMapping("/{id}/addRole")
    @ResponseStatus(HttpStatus.OK)
    public void addRoleToWorkShift(@PathVariable Long id, @RequestBody Map<String, Integer> role) {
        workShiftService.addRole(id, role);
    }

    @DeleteMapping("/{id}/{employeeId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeEmployeeFromWorkShift(@PathVariable Long id, @PathVariable Long employeeId) {
        workShiftService.removeEmployee(id, employeeId);
    }

    @DeleteMapping("/{id}/removeRole")
    @ResponseStatus(HttpStatus.OK)
    public void removeRoleFromWorkShift(@PathVariable Long id, @RequestBody String role) {
        workShiftService.removeRole(id, role);
    }

    @GetMapping("/{id}/info")
    @ResponseStatus(HttpStatus.OK)
    public String getWorkShiftInfo(@PathVariable Long id) {
        return workShiftService.getWorkShiftInfo(id);
    }

    @GetMapping("/employees/{shiftId}")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeesResponse> getAllEmployeesFromShift(@PathVariable Long shiftId) {
        return workShiftService.getAllEmployeeFromShift(shiftId);
    }

}

