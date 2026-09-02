package com.randombank.onboarding;

public class UserAddressInvalidException extends RuntimeException {
    public UserAddressInvalidException() {
        super("User address is invalid");
    }
}
