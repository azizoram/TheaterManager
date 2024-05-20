package workshiftservice.controller;

import workshiftservice.dto.WorkShiftRequest;
import workshiftservice.dto.WorkShiftResponse;

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
    public void createWorkShift(@RequestBody WorkShiftRequest workShiftRequest) {
        workShiftService.createWorkShift(workShiftRequest);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<WorkShiftResponse> getAllWorkShifts() {
        return workShiftService.getAllWorkShifts();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public WorkShiftResponse getWorkShift(@PathVariable Long id) {
        return workShiftService.getWorkShift(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorkShift(@PathVariable Long id) {
        workShiftService.deleteWorkShift(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateWorkShift(@PathVariable Long id, @RequestBody WorkShiftRequest workShiftRequest) {
        workShiftService.updateWorkShift(id, workShiftRequest);
    }

    @PostMapping("/{id}/{employeeEmail}")
    @ResponseStatus(HttpStatus.OK)
    public void addEmployeeToWorkShift(@PathVariable Long id, @PathVariable String employeeEmail) {
        workShiftService.addEmployee(id, employeeEmail);
    }

    @PostMapping("/{id}/addRole")
    @ResponseStatus(HttpStatus.OK)
    public void addRoleToWorkShift(@PathVariable Long id, @RequestBody Map<String, Integer> role) {
        workShiftService.addRole(id, role);
    }

    @DeleteMapping("/{id}/{employeeEmail}")
    @ResponseStatus(HttpStatus.OK)
    public void removeEmployeeFromWorkShift(@PathVariable Long id, @PathVariable String employeeEmail) {
        workShiftService.removeEmployee(id, employeeEmail);
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


}
