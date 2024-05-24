package workshiftservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import workshiftservice.dto.WorkShiftDTO;
import workshiftservice.model.WorkShift;
import workshiftservice.repository.WorkShiftRepository;
import workshiftservice.service.WorkShiftService;
import workshiftservice.exception.NoSuchShiftException;
import java.time.LocalDateTime;
import java.util.*;

class WorkShiftServiceTests {

    @Mock
    private WorkShiftRepository workShiftRepository;

    @InjectMocks
    private WorkShiftService workShiftService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Create workshift test
    @Test
    void createWorkShift_ShouldSaveWorkShift() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(8);
        WorkShiftDTO workShiftDTO = WorkShiftDTO.builder()
                .performanceId(1L)
                .name("Shift 1")
                .description("Description 1")
                .start(start)
                .endTime(end)
                .capacity(10)
                .build();

        workShiftService.createWorkShift(workShiftDTO);

        verify(workShiftRepository, times(1)).save(any(WorkShift.class));
    }

    // Get workshift tests
    @Test
    void getWorkShift_ShouldReturnWorkShiftDTO() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(8);
        WorkShift workShift = WorkShift.builder()
                .id(1L)
                .performanceId(1L)
                .name("Shift 1")
                .description("Description 1")
                .start(start)
                .endTime(end)
                .capacity(10)
                .build();

        when(workShiftRepository.findById(1L)).thenReturn(Optional.of(workShift));

        WorkShiftDTO result = workShiftService.getWorkShift(1L);

        assertEquals("Shift 1", result.getName());
        verify(workShiftRepository, times(1)).findById(1L);
    }

    @Test
    void getWorkShift_ShouldThrowNoSuchShiftException() {
        when(workShiftRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchShiftException.class, () -> workShiftService.getWorkShift(1L));
    }

    @Test
    void getAllWorkShifts_ShouldReturnListOfWorkShiftDTOs() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(8);
        WorkShift workShift = WorkShift.builder()
                .id(1L)
                .performanceId(1L)
                .name("Shift 1")
                .description("Description 1")
                .start(start)
                .endTime(end)
                .capacity(10)
                .build();

        when(workShiftRepository.findAll()).thenReturn(Collections.singletonList(workShift));

        List<WorkShiftDTO> result = workShiftService.getAllWorkShifts();

        assertEquals(1, result.size());
        assertEquals("Shift 1", result.get(0).getName());
        verify(workShiftRepository, times(1)).findAll();
    }


    // Add employee to shift test
    @Test
    void addEmployee_ShouldAddEmployeeToShift() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(8);
        WorkShift workShift = WorkShift.builder()
                .id(1L)
                .performanceId(1L)
                .name("Shift 1")
                .description("Description 1")
                .start(start)
                .endTime(end)
                .capacity(10)
                .employeesCapacity(new HashMap<>())
                .build();

        when(workShiftRepository.findById(1L)).thenReturn(Optional.of(workShift));

        workShiftService.addEmployee(1L, 101L, "Role1");

        assertTrue(workShift.getEmployees().containsKey(101L));
        assertEquals("Role1", workShift.getEmployees().get(101L));
        verify(workShiftRepository, times(1)).save(workShift);
    }

    @Test
    void addEmployee_ShouldThrowExceptionIfEmployeeAlreadyInShift() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(8);
        Map<Long, String> employees = new HashMap<>();
        employees.put(101L, "Role1");

        WorkShift workShift = WorkShift.builder()
                .id(1L)
                .performanceId(1L)
                .name("Shift 1")
                .description("Description 1")
                .start(start)
                .endTime(end)
                .capacity(10)
                .employeesCapacity(employees)
                .build();

        when(workShiftRepository.findById(1L)).thenReturn(Optional.of(workShift));

        assertThrows(RuntimeException.class, () -> workShiftService.addEmployee(1L, 101L, "Role1"));
    }

    // Update workshift test
    @Test
    void updateWorkShift_ShouldUpdateAndSaveWorkShift() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(8);
        WorkShift existingShift = WorkShift.builder()
                .id(1L)
                .performanceId(1L)
                .name("Shift 1")
                .description("Description 1")
                .start(start)
                .endTime(end)
                .capacity(10)
                .build();

        WorkShiftDTO updateDTO = WorkShiftDTO.builder()
                .name("Updated Shift")
                .description("Updated Description")
                .start(start)
                .endTime(end)
                .capacity(20)
                .build();

        when(workShiftRepository.findById(1L)).thenReturn(Optional.of(existingShift));

        workShiftService.updateWorkShift(1L, updateDTO);

        assertEquals("Updated Shift", existingShift.getName());
        assertEquals("Updated Description", existingShift.getDescription());
        assertEquals(20, existingShift.getCapacity());
        verify(workShiftRepository, times(1)).save(existingShift);
    }

    // Delete workshift test
    @Test
    void deleteWorkShift_ShouldDeleteWorkShift() {
        Long shiftId = 1L;

        workShiftService.deleteWorkShift(shiftId);

        verify(workShiftRepository, times(1)).deleteById(shiftId);
    }
}
