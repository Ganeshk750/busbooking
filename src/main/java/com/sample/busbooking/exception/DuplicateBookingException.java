package com.sample.busbooking.exception;

public class DuplicateBookingException extends RuntimeException {

    public DuplicateBookingException(String msg){
        super(msg);
    }
}
