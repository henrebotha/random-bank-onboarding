package com.randombank.onboarding;

public record User(
        String id,
        String username,
        String password,
        String name,
        String address,
        String dateOfBirth,
        String iban,
        AccountType accountType,
        int accountBalanceCents
) {
}