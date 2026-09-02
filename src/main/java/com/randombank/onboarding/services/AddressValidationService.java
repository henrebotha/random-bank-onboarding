package com.randombank.onboarding.services;

import com.randombank.onboarding.Address;

public interface AddressValidationService {
    boolean isValid(Address address);
}
