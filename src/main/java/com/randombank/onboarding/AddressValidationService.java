package com.randombank.onboarding;

public interface AddressValidationService {
    boolean isValid(String country, String postalCode, String streetAddress);
}
