package com.hcltech.busbooking.exception;

public class BookingCancelledException extends RuntimeException{

    public BookingCancelledException(String msg) {
        super(msg);
    }
}
