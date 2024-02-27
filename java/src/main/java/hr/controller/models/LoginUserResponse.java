package hr.controller.models;

import hr.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginUserResponse {
    private String id;
    private UserRole role;
}
