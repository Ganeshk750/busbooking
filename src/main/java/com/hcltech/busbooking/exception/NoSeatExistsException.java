package com.hcltech.busbooking.exception;

public class NoSeatExistsException extends RuntimeException{
    private String message;

    public NoSeatExistsException() {}

    public NoSeatExistsException(String msg) {
        super(msg);
        this.message = msg;
    }
}
