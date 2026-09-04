package com.randombank.onboarding.exceptions;

public class NotAuthenticatedException extends RuntimeException {
    public NotAuthenticatedException() {
        super("Not logged in");
    }
}
