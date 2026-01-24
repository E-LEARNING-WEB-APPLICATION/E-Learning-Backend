package com.learnease.server.exception.custom_exception;


import com.learnease.server.model.enums.BookingErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BookingException extends RuntimeException{

    private final BookingErrorCode errorCode;

    public BookingException(BookingErrorCode errorCode , String message){
        super(message);
        this.errorCode = errorCode;
    };
}
