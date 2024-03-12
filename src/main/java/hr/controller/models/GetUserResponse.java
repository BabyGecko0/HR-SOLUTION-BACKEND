package hr.controller.models;


import hr.enums.Status;
import hr.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserResponse {
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private UserRole role;
    private Long daysOff;
    private List<TimesheetDetails> timesheetDetails;

    @Data
    @AllArgsConstructor
    public static class TimesheetDetails{
        private LocalDateTime fromDate;
        private LocalDateTime toDate;
        private String note;
        private Status status;
        private LocalDateTime createdAt;
        private String createdBy;
        private LocalDateTime modifiedAt;
        private String modifiedBy;
    }
}
