package hr.controller;

import hr.exceptions.*;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(0)
public class ExceptionHandlers {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    private ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException){
        return new ResponseEntity<>(
                new ApiError(methodArgumentNotValidException.getMessage(), "409"),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler({UserNotFoundException.class})
    private ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException notFoundException){
        return new ResponseEntity<>(
                new ApiError(notFoundException.getMessage(), "404"),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler({TimesheetNotFoundException.class})
    private ResponseEntity<ApiError> handleTimesheetNotFoundException(TimesheetNotFoundException timesheetNotFoundException){
        return new ResponseEntity<>(
                new ApiError(timesheetNotFoundException.getMessage(), "404"),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler({UserAuthenticationException.class})
    private ResponseEntity<ApiError> handleUserAuthenticationException(UserAuthenticationException userAuthenticationException){
        return new ResponseEntity<>(
                new ApiError(userAuthenticationException.getMessage(), "401"),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler({UserAuthorizationException.class})
    private ResponseEntity<ApiError> handleUserAuthorizationException(UserAuthorizationException userAuthorizationException){
        return new ResponseEntity<>(
                new ApiError(userAuthorizationException.getMessage(), "403"),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler({DuplicatedTimesheetDatesException.class})
    private ResponseEntity<ApiError> handleDuplicatedTimesheetDatesException(DuplicatedTimesheetDatesException duplicatedTimesheetDatesException){
        return new ResponseEntity<>(
                new ApiError(duplicatedTimesheetDatesException.getMessage(), "409"),
                HttpStatus.CONFLICT
        );
    }
}
