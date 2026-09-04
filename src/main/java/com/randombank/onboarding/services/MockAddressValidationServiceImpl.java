package com.randombank.onboarding.services;

import com.randombank.onboarding.Address;
import org.springframework.stereotype.Service;

@Service
public class MockAddressValidationServiceImpl implements AddressValidationService {
    @Override
    public boolean isValid(Address address) {
        return true;
    }
}
