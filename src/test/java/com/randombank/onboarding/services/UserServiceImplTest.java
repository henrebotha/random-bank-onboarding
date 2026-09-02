package com.randombank.onboarding.services;

import com.randombank.onboarding.AccountType;
import com.randombank.onboarding.Address;
import com.randombank.onboarding.CreateUserDTO;
import com.randombank.onboarding.UserRepository;
import com.randombank.onboarding.exceptions.UserAddressInvalidException;
import com.randombank.onboarding.exceptions.UserAlreadyExistsException;
import com.randombank.onboarding.exceptions.UserCountryInvalidException;
import com.randombank.onboarding.exceptions.UserTooYoungException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceImplTest {
    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private AddressValidationServiceImpl addressValidationService;

    @Nested
    class UserServiceImplTestCreateUser {
        private final String GOOD_COUNTRY = "NL";

        private final CreateUserDTO createUserDTO = new CreateUserDTO(
                "joe",
                "Joseph",
                new Address(GOOD_COUNTRY, "2011JV", "Bakenessergracht 81"),
                "2000-01-01",
                AccountType.CURRENT
        );

        @BeforeEach
        void setUp() {
            when(addressValidationService.isValid(any(Address.class))).thenReturn(true);
        }

        @Test
        void create() {
            assertDoesNotThrow(() -> userService.create(createUserDTO));
        }

        @Test
        void createTooYoung() {
            CreateUserDTO user = new CreateUserDTO(
                    createUserDTO.username(),
                    createUserDTO.name(),
                    createUserDTO.address(),
                    "2020-01-01",
                    createUserDTO.accountType()
            );
            assertThrows(UserTooYoungException.class, () -> userService.create(user));
        }

        @Test
        void createInvalidCountry() {
            when(addressValidationService.isValid(any(Address.class))).thenThrow(UserCountryInvalidException.class);

            assertThrows(UserCountryInvalidException.class, () -> userService.create(createUserDTO));
        }

        @Test
        void createInvalidAddress() {
            when(addressValidationService.isValid(any(Address.class))).thenThrow(UserAddressInvalidException.class);

            assertThrows(UserAddressInvalidException.class, () -> userService.create(createUserDTO));
        }

        @Test
        void createAlreadyExists() {
            CreateUserDTO user = new CreateUserDTO(
                    "alice",
                    createUserDTO.name(),
                    createUserDTO.address(),
                    createUserDTO.dateOfBirth(),
                    createUserDTO.accountType()
            );
            assertThrows(UserAlreadyExistsException.class, () -> userService.create(user));
        }
    }

    @Nested
    class UserServiceImplTestFind {
        @Test
        void findAll() {
        }

        @Test
        void findByUsername() {
        }

        @Test
        void findById() {
        }
    }
}
