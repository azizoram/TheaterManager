package cz.cvut.fel.perfomanceservice.repository;

import cz.cvut.fel.perfomanceservice.model.Performance;
import cz.cvut.fel.workshiftservice.model.WorkShift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {


    public Optional<Performance> findById(Long id);
}
