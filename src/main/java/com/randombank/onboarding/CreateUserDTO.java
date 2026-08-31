package com.randombank.onboarding;

public record CreateUserDTO(
        String username,
        String name,
        String address,
        String dateOfBirth,
        AccountType accountType
) {
}