package com.randombank.onboarding.exceptions;

public class DbOccupiedException extends RuntimeException {
    public DbOccupiedException() {
        super("Database busy; try again later");
    }
}
