package hr.controller.models;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimesheetRequest {
    private LocalDateTime fromDate;

    private LocalDateTime toDate;

    private String note;
}
