package hr.controller.models;

import hr.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EditTimesheetRequest {
    private String username;
    private String firstName;
    private String lastName;
    private Status status;
}
