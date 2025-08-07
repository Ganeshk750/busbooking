package com.hcltech.busbooking.exception;

public class NoSeatExistsException extends RuntimeException{
    String message;

    public NoSeatExistsException(String msg) {
        super(msg);
        this.message = msg;
    }
}
