package hr.repository;

import hr.entity.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TimesheetRepository extends JpaRepository<Timesheet, String> {
    List<Timesheet> findAllByUserId(String userId);

    boolean existsByUserIdAndFromDate(String userId, LocalDate fromDate);
    boolean existsByUserIdAndToDate(String userId, LocalDate toDate);
}
