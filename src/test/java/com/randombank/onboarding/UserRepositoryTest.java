package com.randombank.onboarding;

import com.randombank.onboarding.exceptions.DbOccupiedException;
import com.randombank.onboarding.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserRepositoryTest {
    @Value("${random-bank-onboarding.throttle-db:false}")
    private boolean throttleDb;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
    }

    @Test
    /*
    This test is supposed to verify the "slow DB" simulation I attempted. The test fails because the simulation doesn't
    really work. See ThrottleInterceptor for the implementation.
     */
    void throttleDb() {
        if (throttleDb) {
            userRepository.findByUsername("bob");
            assertThrows(DbOccupiedException.class, () -> userRepository.findByUsername("carol"));
        }
    }
}
