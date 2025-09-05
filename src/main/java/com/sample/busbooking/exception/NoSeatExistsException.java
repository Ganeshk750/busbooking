package com.sample.busbooking.exception;

public class NoSeatExistsException extends RuntimeException{

    public NoSeatExistsException(String msg) {
        super(msg);
    }
}
