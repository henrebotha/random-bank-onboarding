package com.randombank.onboarding.exceptions;

public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException() {
        super("Incorrect password");
    }
}
