package com.randombank.onboarding;

import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final List<User> users = new ArrayList<>();

    private static final String DEFAULT_PASSWORD = "1234";
    private static final String BANK_CODE = "RNDB";

    public UserServiceImpl() {
        // Sample data for users
        users.add(new User(
                "77777777-7777-7777-7777-777777777777",
                "alice",
                DEFAULT_PASSWORD,
                "Alice",
                "Bakenessergracht 87, 2011JV",
                "1990-01-20",
                "asdf",
                AccountType.CURRENT,
                0
        ));
        users.add(new User(
                "88888888-8888-8888-8888-888888888888",
                "bobert",
                DEFAULT_PASSWORD,
                "Bob",
                "Bakenessergracht 83, 2011JV",
                "1992-05-17",
                "asdg",
                AccountType.CURRENT,
                0
        ));
        users.add(new User(
                "99999999-9999-9999-9999-999999999999",
                "carolx",
                DEFAULT_PASSWORD,
                "Carol",
                "Bakenessergracht 81, 2011JV",
                "1981-12-20",
                "asdj",
                AccountType.SAVINGS,
                0
        ));
    }

    @Override
    public User createUser(CreateUserDTO user) {
        // Validate:
        //   username must be unique
        //   address must be NL/BE
        //   age must be >= 18
        // Generate:
        //   IBAN per NL format
        //   default password

        logger.info("User: {}", user);
        CountryCode country = null;
        if (user.address().matches(".*\\bNL\\b.*")) {
            country = CountryCode.NL;
        } else if (user.address().matches(".*\\bBE\\b.*")) {
            country = CountryCode.BE;
        }

        if (country == null) {
            throw new UserCountryInvalidException();
        }

        LocalDate dateOfBirth = LocalDate.parse(user.dateOfBirth());
        if (dateOfBirth.isAfter(LocalDate.now().minusYears(18))) {
            throw new UserTooYoungException();
        }

        if (users.stream().anyMatch(existingUser -> existingUser.username().equals(user.username()))) {
            throw new UserAlreadyExistsException();
        }

        String newUuid = UUID.randomUUID().toString();

        String iban = new Iban.Builder().countryCode(country).bankCode(BANK_CODE).buildRandom().toString();

        User newUser = new User(
                newUuid,
                user.username(),
                DEFAULT_PASSWORD,
                user.name(),
                user.address(),
                user.dateOfBirth(),
                iban,
                user.accountType(),
                0
        );
        users.add(newUser);
        return newUser;
    }

    @Override
    public List<User> findAllUsers() {
        return users;
    }

    @Override
    public User findUserById(String id) {
        return users.stream().filter(user -> user.id().equals(id)).findFirst().orElse(null);
    }

    @Override
    public void deleteAllUsers() {
        users.clear();
    }
}