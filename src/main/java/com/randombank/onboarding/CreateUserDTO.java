package com.randombank.onboarding;

public record CreateUserDTO(
        String username,
        String name,
        String country,
        String postalCode,
        String streetAddress,
        String dateOfBirth,
        AccountType accountType
) {
}