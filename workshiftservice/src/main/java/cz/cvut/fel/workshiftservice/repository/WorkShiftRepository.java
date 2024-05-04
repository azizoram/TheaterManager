package cz.cvut.fel.workshiftservice.repository;

import cz.cvut.fel.workshiftservice.model.WorkShift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkShiftRepository extends JpaRepository<WorkShift, Long>{

}
