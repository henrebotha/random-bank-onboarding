package com.randombank.onboarding;

public record CreateUserDTO(
        String username,
        String name,
        Address address,
        String dateOfBirth,
        AccountType accountType
) {
}