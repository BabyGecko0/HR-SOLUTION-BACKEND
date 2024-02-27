package hr.controller.models;

import hr.enums.Status;
import hr.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GetAllTimesheetResponse {
    private String id;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String note;
    private Status status;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
    private UserDetails userDetails;

    @Data
    @AllArgsConstructor
    public static class UserDetails {
        private String username;
        private String firstName;
        private String lastName;
        private String password;
        private UserRole role;
        private Long daysOff;
    }
}
