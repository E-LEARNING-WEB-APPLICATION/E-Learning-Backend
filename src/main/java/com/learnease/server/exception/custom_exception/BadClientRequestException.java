package com.learnease.server.exception.custom_exception;

public class BadClientRequestException extends RuntimeException{
    public BadClientRequestException(String message) {super(message);}
}
