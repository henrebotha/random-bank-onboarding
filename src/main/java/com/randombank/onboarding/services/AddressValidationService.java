package com.randombank.onboarding.services;

public interface AddressValidationService {
    boolean isValid(String country, String postalCode, String streetAddress);
}
