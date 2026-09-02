package com.randombank.onboarding;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
class LoginServiceImplTest {
    @Autowired
    private LoginService loginService;

    @MockitoBean
    private UserService userService;

    private final String GOOD_PASSWORD = "1234";
    private final String BAD_PASSWORD = "1235";
    private final String GOOD_USERNAME = "alice";
    private final String BAD_USERNAME = "alife";

    @BeforeEach
    void setUp() {
        final User user = new User(
                UUID.fromString("77777777-7777-7777-7777-777777777777"),
                GOOD_USERNAME,
                GOOD_PASSWORD,
                "Alice",
                "NL",
                "2011JV",
                "Bakenessergracht 87",
                "1990-01-20",
                "asdf",
                AccountType.CURRENT,
                0
        );
        when(userService.findByUsername(eq(GOOD_USERNAME))).thenReturn(user);
        when(userService.findByUsername(eq(BAD_USERNAME))).thenReturn(null);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void login() {
        assertDoesNotThrow(() -> loginService.login(GOOD_USERNAME, GOOD_PASSWORD));
    }

    @Test
    void loginUnknownUser() {
        assertThrows(UserNotFoundException.class, () -> loginService.login(BAD_USERNAME, GOOD_PASSWORD));
    }

    @Test
    void loginIncorrectPassword() {
        assertThrows(IncorrectPasswordException.class, () -> loginService.login(GOOD_USERNAME, BAD_PASSWORD));
    }
}