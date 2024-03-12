package hr.controller;

import hr.controller.models.*;
import hr.exceptions.UserNotFoundException;
import hr.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.naming.AuthenticationException;
import java.text.ParseException;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<GetUserResponse> getById(@PathVariable(value = "id") String id) throws UserNotFoundException {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> login(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) throws UserNotFoundException {
        return ResponseEntity.ok(userService.validateUser(username, password));
    }

    @PostMapping("/signUp")
    public ResponseEntity<UserResponse> signUp(@Valid @RequestBody UserRequest userRequest) throws AuthenticationException {
        return ResponseEntity.ok(userService.signUp(userRequest));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<UserResponse> edit(@PathVariable (value = "id") String id, @RequestBody EditUserRequest editUserRequest)throws UserNotFoundException,ParseException {
        return ResponseEntity.ok(userService.edit(id,editUserRequest));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        userService.deleteById(id);
    }
}
