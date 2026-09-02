package com.randombank.onboarding.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Username not found");
    }
}
