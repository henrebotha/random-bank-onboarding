package com.randombank.onboarding;

public class UserTooYoungException extends RuntimeException {
    public UserTooYoungException() {
        super("User is not of age");
    }
}
