package com.randombank.onboarding.services;

import com.randombank.onboarding.Address;
import com.randombank.onboarding.exceptions.UserCountryInvalidException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@SpringBootTest
class AddressValidationServiceImplTest {
    @Autowired
    private AddressValidationServiceImpl addressValidationService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void isValid() {
        assertTrue(addressValidationService.isValid(new Address("NL", "2011JV", "Bakenessergracht 81")));
    }

    @Test
    void isValidBelgium() {
        assertTrue(addressValidationService.isValid(new Address("BE", "2018", "Koningin Astridplein 20")));
    }

    @Test
    void isValidInvalidCountry() {
        assertThrows(
                UserCountryInvalidException.class,
                () -> addressValidationService.isValid(new Address("FR", "2011JV", "Bakenessergracht 81"))
        );
    }

    @Test
    void isValidInvalidAddress() {
        AddressValidationServiceImpl spy = spy(addressValidationService);
        Address address = new Address("NL", "2011JV", "Bakenessergracht 1");

        doReturn(List.of("c")).when(spy).fetchGrades(eq(address));

        assertFalse(spy.isValid(address));
    }
}
