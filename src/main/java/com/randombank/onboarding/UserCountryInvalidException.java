package com.randombank.onboarding;

public class UserCountryInvalidException extends RuntimeException {
    public UserCountryInvalidException() {
        super("User address is not in valid country");
    }
}
