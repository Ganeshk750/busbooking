package com.hcltech.busbooking.exception;

public class BookingCancelledException extends RuntimeException{

    String message;

    public BookingCancelledException(String msg) {
        super(msg);
        this.message = msg;
    }
}
