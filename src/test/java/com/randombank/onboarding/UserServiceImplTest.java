package com.randombank.onboarding;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserServiceImplTest {
    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @Nested
    class UserServiceImplTestCreateUser {
        private final CreateUserDTO createUserDTO = new CreateUserDTO(
                "joe",
                "Joseph",
                "NL",
                "2011JV",
                "Bakenessergracht 81",
                "2000-01-01",
                AccountType.CURRENT
        );

        @BeforeEach
        void setUp() {
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
                    createUserDTO.country(),
                    createUserDTO.postalCode(),
                    createUserDTO.streetAddress(),
                    "2020-01-01",
                    createUserDTO.accountType()
            );
            assertThrows(UserTooYoungException.class, () -> userService.create(user));
        }

        @Test
        void createInvalidCountry() {
            CreateUserDTO user = new CreateUserDTO(
                    createUserDTO.username(),
                    createUserDTO.name(),
                    "FR",
                    createUserDTO.postalCode(),
                    createUserDTO.streetAddress(),
                    createUserDTO.dateOfBirth(),
                    createUserDTO.accountType()
            );
            assertThrows(UserCountryInvalidException.class, () -> userService.create(user));
        }

        @Test
        void createAlreadyExists() {
            CreateUserDTO user = new CreateUserDTO(
                    "alice",
                    createUserDTO.name(),
                    createUserDTO.country(),
                    createUserDTO.postalCode(),
                    createUserDTO.streetAddress(),
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