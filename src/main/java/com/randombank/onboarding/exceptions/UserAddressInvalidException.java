package com.randombank.onboarding.exceptions;

public class UserAddressInvalidException extends RuntimeException {
    public UserAddressInvalidException() {
        super("User address is invalid");
    }
}
