package com.randombank.onboarding.exceptions;

public class UserCountryInvalidException extends RuntimeException {
    public UserCountryInvalidException() {
        super("User is not in valid country");
    }
}
