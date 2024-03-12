package hr.controller;

import hr.controller.models.*;
import hr.exceptions.DuplicatedTimesheetDatesException;
import hr.exceptions.TimesheetNotFoundException;
import hr.exceptions.UserAuthorizationException;
import hr.exceptions.UserNotFoundException;
import hr.service.TimesheetService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/timesheet")
@AllArgsConstructor
public class TimesheetController {
    private TimesheetService timesheetService;

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<GetAllTimesheetResponse>> getAll(@PathVariable(value = "userId") String userId) throws UserAuthorizationException, UserNotFoundException {
        return ResponseEntity.ok(timesheetService.findAll(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetTimesheetResponse> getById(@PathVariable(value = "id") String id) throws TimesheetNotFoundException {
        return ResponseEntity.ok(timesheetService.findById(id));
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<TimesheetResponse> create(@PathVariable(value = "userId") String userId, @RequestBody TimesheetRequest timesheetRequest) throws UserNotFoundException, UserAuthorizationException, DuplicatedTimesheetDatesException {
        return ResponseEntity.ok(timesheetService.save(userId, timesheetRequest));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteTimesheet(@PathVariable(value = "id") String id) throws TimesheetNotFoundException {
        timesheetService.deleteById(id);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<TimesheetResponse> edit(@PathVariable(value = "id") String id, @RequestBody EditTimesheetRequest editTimesheetRequest) throws TimesheetNotFoundException, ParseException {
        return ResponseEntity.ok(timesheetService.edit(id, editTimesheetRequest));
    }
}
