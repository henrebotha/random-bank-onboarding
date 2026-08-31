package com.randombank.onboarding;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Username not found");
    }
}
