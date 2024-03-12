package hr.controller.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EditUserRequest {
    private String username;
    private String firstName;
    private String lastName;
}
