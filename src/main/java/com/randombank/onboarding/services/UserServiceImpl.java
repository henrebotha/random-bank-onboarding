package com.randombank.onboarding.services;

import com.randombank.onboarding.CreateUserDTO;
import com.randombank.onboarding.User;
import com.randombank.onboarding.UserRepository;
import com.randombank.onboarding.exceptions.UserAddressInvalidException;
import com.randombank.onboarding.exceptions.UserAlreadyExistsException;
import com.randombank.onboarding.exceptions.UserTooYoungException;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository repository;
    @Autowired
    private AddressValidationService addressValidationService;

    @Value("${random-bank-onboarding.valid-countries}")
    private List<String> validCountries;

    private static final String DEFAULT_PASSWORD = "1234";
    private static final String BANK_CODE = "RNDB";

    @Autowired
    public UserServiceImpl(UserRepository repository, AddressValidationService addressValidationService) {
        this.repository = repository;
        this.addressValidationService = addressValidationService;
        this();
    }

    public UserServiceImpl() {
        logger.info("Users in db: {}", findAll());
    }

    @Override
    public User create(CreateUserDTO user) {
        logger.info("User: {}", user);

        if (!addressValidationService.isValid(user.address())) {
            throw new UserAddressInvalidException();
        }

        CountryCode countryCode = CountryCode.getByCode(user.address().country());
        logger.info("Found country code {} from input {}", countryCode, user.address().country());

        LocalDate dateOfBirth = LocalDate.parse(user.dateOfBirth());
        if (dateOfBirth.isAfter(LocalDate.now().minusYears(18))) {
            throw new UserTooYoungException();
        }

        if (repository.existsUserByUsername(user.username())) {
            throw new UserAlreadyExistsException();
        }

        String iban = new Iban.Builder().countryCode(countryCode).bankCode(BANK_CODE).buildRandom().toString();

        User newUser = new User(
                user.username(),
                DEFAULT_PASSWORD,
                user.name(),
                user.address(),
                user.dateOfBirth(),
                iban,
                user.accountType(),
                0
        );
        return repository.save(newUser);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public User findById(UUID id) {
        return repository.findById(id).orElse(null);
    }
}
