package com.learnease.server.exception;


import com.learnease.server.dto.ApiResponse;
import com.learnease.server.exception.custom_exception.*;
import com.learnease.server.model.enums.BookingErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException e){
        String errorMessage = e.getFieldErrors()
                .get(0)
                .getDefaultMessage();

        ApiResponse response = new ApiResponse(false, errorMessage);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<?> handleEmailExists(EmailAlreadyExistsException e){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiResponse(false , e.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse(false , "Invalid Email or Password"));
    };

    @ExceptionHandler(BadClientRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadClientRequestException ex){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<?> handleFileStorageException(FileStorageException ex){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleUUIDMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(false, "Invalid UUID format"));
    }

    @ExceptionHandler(BookingException.class)
    public ResponseEntity<?> handleBookingException(BookingException ex){
        HttpStatus status = mapToHttpStatus(ex.getErrorCode()); //used for mapping differnt errorcode with actual http response codes
        return ResponseEntity
                .status(status)
                .body(new ApiResponse(false, ex.getMessage()));
    }

    private HttpStatus mapToHttpStatus(BookingErrorCode errorCode) {

        return switch (errorCode) {

            case USER_NOT_FOUND -> HttpStatus.UNAUTHORIZED;
            case USER_NOT_ACTIVE -> HttpStatus.FORBIDDEN;
            case USER_NOT_STUDENT -> HttpStatus.FORBIDDEN;

            case STUDENT_PROFILE_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case COURSE_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case INSTRUCTOR_NOT_FOUND -> HttpStatus.NOT_FOUND;

            case BOOKING_ALREADY_PAID -> HttpStatus.CONFLICT;
            case BOOKING_EXPIRED -> HttpStatus.GONE;

            case PAYMENT_ORDER_CREATION_FAILED -> HttpStatus.BAD_GATEWAY;
            case PAYMENT_VERIFICATION_FAILED -> HttpStatus.BAD_REQUEST;

            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(false, ex.getMessage())); // for developement later change to generic message
    }

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<?> handleCourseNotFoundException(CourseNotFoundException ex){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, ex.getMessage())); // for developement later change to generic message
    }
}
