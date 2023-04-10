package com.denisfesenko.exception;

public class OpenAiException extends RuntimeException {
    public OpenAiException(Throwable cause) {
        super(cause);
    }
}
