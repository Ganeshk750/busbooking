package com.hcltech.busbooking.exception;

public class DuplicateBookingException extends RuntimeException {

    public DuplicateBookingException(String msg){
        super(msg);
    }
}
