package com.randombank.onboarding;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@SpringBootTest
class AddressValidationServiceImplTest {
    @Autowired
    private AddressValidationServiceImpl addressValidationService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        try {
            closeable.close();
        } catch (Exception e) {
            throw new RuntimeException("Test error", e);
        }
    }

    @Test
    void isValid() {
        assertDoesNotThrow(() -> addressValidationService.isValid("NL", "2011JV", "Bakenessergracht 81"));
    }

    @Test
    void isValidInvalidCountry() {
        assertThrows(
                UserCountryInvalidException.class,
                () -> addressValidationService.isValid("FR", "2011JV", "Bakenessergracht 81")
        );
    }

    @Test
    void isValidInvalidAddress() {
        AddressValidationServiceImpl spy = spy(addressValidationService);
        when(spy.getGrades(anyString(), anyString(), anyString())).thenReturn(List.of("c"));

        assertFalse(spy.isValid("NL", "2011JV", "Bakenessergracht 1"));
    }
}
