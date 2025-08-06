package com.hcltech.busbooking.exception;

public class BookingCancelledException extends RuntimeException{
    private String message;

    public BookingCancelledException() {}

    public BookingCancelledException(String msg) {
        super(msg);
        this.message = msg;
    }
}
