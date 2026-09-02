package com.randombank.onboarding.exceptions;

public class UserTooYoungException extends RuntimeException {
    public UserTooYoungException() {
        super("User is not of age");
    }
}
