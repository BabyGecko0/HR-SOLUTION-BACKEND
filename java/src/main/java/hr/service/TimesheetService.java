package hr.service;

import hr.controller.models.*;
import hr.enums.Status;
import hr.enums.UserRole;
import hr.exceptions.DuplicatedTimesheetDatesException;
import hr.exceptions.TimesheetNotFoundException;
import hr.exceptions.UserAuthorizationException;
import hr.exceptions.UserNotFoundException;
import hr.entity.Timesheet;
import hr.entity.User;
import hr.repository.TimesheetRepository;
import hr.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TimesheetService {

    private TimesheetRepository timesheetRepository;
    private UserRepository userRepository;

    public GetTimesheetResponse findById(String id) throws TimesheetNotFoundException {
       Timesheet timesheet = timesheetRepository.findById(id).orElseThrow(() -> new TimesheetNotFoundException("Timesheet not found"));
       return new GetTimesheetResponse(
               timesheet.getFromDate(),
               timesheet.getToDate(),
               timesheet.getNote(),
               timesheet.getStatus(),
               timesheet.getCreatedAt(),
               timesheet.getCreatedBy(),
               timesheet.getModifiedAt(),
               timesheet.getModifiedBy(),
               new GetTimesheetResponse.UserDetails(
                       timesheet.getUser().getUsername(),
                       timesheet.getUser().getFirstName(),
                       timesheet.getUser().getLastName(),
                       timesheet.getUser().getDaysOff())
       );
    }

    public List<GetAllTimesheetResponse> findAll(String userId) throws UserAuthorizationException, UserNotFoundException {
        if (isUserManager(userId)) return timesheetRepository.findAll().stream().map(
                timesheet -> new GetAllTimesheetResponse(
                        timesheet.getId(),
                        timesheet.getFromDate(),
                        timesheet.getToDate(),
                        timesheet.getNote(),
                        timesheet.getStatus(),
                        timesheet.getCreatedAt(),
                        timesheet.getCreatedBy(),
                        timesheet.getModifiedAt(),
                        timesheet.getModifiedBy(),
                        new GetAllTimesheetResponse.UserDetails(
                                timesheet.getUser().getUsername(),
                                timesheet.getUser().getFirstName(),
                                timesheet.getUser().getLastName(),
                                timesheet.getUser().getPassword(),
                                timesheet.getUser().getRole(),
                                timesheet.getUser().getDaysOff())
                )).toList();
        throw new UserAuthorizationException("Only MANAGER are allowed to see this content");
    }

    public TimesheetResponse save(String userId, TimesheetRequest timesheetRequest) throws UserNotFoundException, UserAuthorizationException, DuplicatedTimesheetDatesException {
            if (!isUserManager(userId)) {
            return new TimesheetResponse(timesheetRepository.save(mapToTimeSheet(userId, timesheetRequest)).getId());
        } else {
            throw new UserAuthorizationException("Only USER are allowed to create timesheet");
        }
    }

    public void deleteById(String id) throws TimesheetNotFoundException {
        timesheetRepository.deleteById(timesheetRepository.findById(id).orElseThrow(() -> new TimesheetNotFoundException("Timesheet not found")).getId());
    }

    public TimesheetResponse edit(String id, EditTimesheetRequest editTimesheetRequest) throws TimesheetNotFoundException, ParseException {
        Timesheet timesheet = timesheetRepository.findById(id).orElseThrow(() -> new TimesheetNotFoundException("Timesheet not found"));
        if(editTimesheetRequest.getStatus() != null){
            timesheet.setStatus(editTimesheetRequest.getStatus());
            timesheetRepository.save(timesheet);
        }
        User user = updateUser(editTimesheetRequest, timesheet);
        return new TimesheetResponse(userRepository.save(user).getId());
    }



    public Timesheet mapToTimeSheet(String userId, TimesheetRequest timesheetRequest) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Timesheet timesheet = new Timesheet();
        timesheet.setFromDate(timesheetRequest.getFromDate());
        timesheet.setToDate(timesheetRequest.getToDate());
        timesheet.setNote(timesheetRequest.getNote());
        timesheet.setUser(user);
        timesheet.setCreatedAt(LocalDateTime.now());
        timesheet.setCreatedBy(user.getUsername());
        timesheet.setModifiedAt(LocalDateTime.now());
        timesheet.setModifiedBy(user.getUsername());
        return timesheetRepository.save(timesheet);
    }

    public boolean isUserManager(String userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        return user.getRole().equals(UserRole.MANAGER);
    }

    private User updateUser(EditTimesheetRequest editTimesheetRequest, Timesheet timesheet) throws ParseException {
        User user = timesheet.getUser();
        user.setUsername(editTimesheetRequest.getUsername() != null ? editTimesheetRequest.getUsername() : user.getUsername());
        user.setLastName(editTimesheetRequest.getLastName() != null ? editTimesheetRequest.getLastName() : user.getLastName());
        user.setFirstName(editTimesheetRequest.getFirstName() != null ? editTimesheetRequest.getFirstName() : user.getFirstName());
        if(editTimesheetRequest.getStatus() == Status.ACTIVE) user.setDaysOff(
                calculateDayOffs(user.getDaysOff(), timesheet.getFromDate(), timesheet.getToDate())
        );
        return user;
    }

    private long calculateDayOffs(Long totalDaysOff, LocalDateTime fromDate, LocalDateTime toDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date firstDate = sdf.parse(ChronoLocalDate.from(fromDate).toString());
        Date secondDate = sdf.parse(ChronoLocalDate.from(toDate).toString());

        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        long totalDays = totalDaysOff - TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return totalDays >= 0 ? totalDays : 0;
    }
}
