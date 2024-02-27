package hr.service;

import hr.controller.models.*;
import hr.enums.UserRole;
import hr.exceptions.UserNotFoundException;
import hr.entity.Timesheet;
import hr.entity.User;
import hr.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public GetUserResponse findById(String id) throws UserNotFoundException {
        return mapUserResponse(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found")));
    }

    public LoginUserResponse validateUser(String username, String password) throws UserNotFoundException {
        User user = userRepository.findByUsernameAndPassword(username, password).orElseThrow(() -> new UserNotFoundException("User credentials are not correct!"));
        return new LoginUserResponse(user.getId(), user.getRole());
    }

    public UserResponse signUp(UserRequest userRequest) throws AuthenticationException {
        if(userRepository.findByUsername(userRequest.getUsername()).isPresent()) throw new AuthenticationException("Username already exists, try another one");
        return new UserResponse(userRepository.save(mapToDefaultUser(userRequest)).getId());
    }

    public UserResponse edit(String id, EditUserRequest editUserRequest) throws UserNotFoundException, ParseException {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        user.setFirstName(editUserRequest.getFirstName());
        user.setLastName(editUserRequest.getLastName());
        user.setUsername(editUserRequest.getUsername());

        userRepository.save(user);

        return new UserResponse(user.getId());
    }

    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    public User mapToDefaultUser(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setPassword(userRequest.getPassword());
        user.setRole(UserRole.USER);
        user.setCreatedBy(userRequest.getUsername());
        user.setCreatedAt(LocalDateTime.now());
        user.setModifiedBy(userRequest.getUsername());
        user.setModifiedAt(LocalDateTime.now());
        return user;
    }

    public GetUserResponse mapUserResponse(User user) {
        return new GetUserResponse(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                user.getRole(),
                user.getDaysOff(),
                user.getTimesheets().stream().map(
                        timesheet -> new GetUserResponse.TimesheetDetails(
                                timesheet.getFromDate(),
                                timesheet.getToDate(),
                                timesheet.getNote(),
                                timesheet.getStatus(),
                                timesheet.getCreatedAt(),
                                timesheet.getCreatedBy(),
                                timesheet.getModifiedAt(),
                                timesheet.getModifiedBy()
                        )
                ).toList()
        );
    }
}
