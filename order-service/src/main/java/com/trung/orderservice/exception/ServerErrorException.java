package com.trung.orderservice.exception;

public class ServerErrorException extends Exception{
    public ServerErrorException(String message) {
        super(message);
    }
}
