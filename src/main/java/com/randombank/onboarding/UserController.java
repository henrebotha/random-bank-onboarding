package com.randombank.onboarding;

import com.randombank.onboarding.exceptions.IncorrectPasswordException;
import com.randombank.onboarding.exceptions.UserAddressInvalidException;
import com.randombank.onboarding.exceptions.UserAlreadyExistsException;
import com.randombank.onboarding.exceptions.UserCountryInvalidException;
import com.randombank.onboarding.exceptions.UserNotFoundException;
import com.randombank.onboarding.exceptions.UserTooYoungException;
import com.randombank.onboarding.services.LoginService;
import com.randombank.onboarding.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class UserController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/register")
    public User register(
            @RequestBody CreateUserDTO user
    ) {
        // Validate:
        //   username must be unique
        //   country must be NL/BE
        //   age must be >= 18
        // Generate:
        //   IBAN per NL format
        //   default password

        logger.info("/register request: {}", user.toString());
        return userService.create(user);
    }

    @GetMapping("/overview/{id}")
    public User overview(
            @PathVariable UUID id
    ) {
        logger.info("/overview request: {}", id);
        return userService.findById(id);
    }

    @GetMapping("/login")
    public User login(@RequestParam String username, @RequestParam String password) {
        logger.info("/login request: {} - {}", username, password);
        return loginService.login(username, password);
    }

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    @ResponseStatus(
            HttpStatus.CONFLICT
    )
    private Map<String, String> handleUserAlreadyExistsException(
            UserAlreadyExistsException e
    ) {
        Map<String, String> body = new HashMap<>();
        body.put("error", e.getMessage());
        return body;
    }

    @ExceptionHandler(value = UserCountryInvalidException.class)
    @ResponseStatus(
            HttpStatus.BAD_REQUEST
    )
    private Map<String, String> handleUserCountryInvalidException(
            UserCountryInvalidException e
    ) {
        Map<String, String> body = new HashMap<>();
        body.put("error", e.getMessage());
        return body;
    }

    @ExceptionHandler(value = UserAddressInvalidException.class)
    @ResponseStatus(
            HttpStatus.BAD_REQUEST
    )
    private Map<String, String> handleUserAddressInvalidException(
            UserAddressInvalidException e
    ) {
        Map<String, String> body = new HashMap<>();
        body.put("error", e.getMessage());
        return body;
    }

    @ExceptionHandler(value = UserTooYoungException.class)
    @ResponseStatus(
            HttpStatus.BAD_REQUEST
    )
    private Map<String, String> handleUserTooYoungException(
            UserTooYoungException e
    ) {
        Map<String, String> body = new HashMap<>();
        body.put("error", e.getMessage());
        return body;
    }

    @ExceptionHandler(value = IncorrectPasswordException.class)
    @ResponseStatus(
            HttpStatus.UNAUTHORIZED
    )
    private Map<String, String> handleIncorrectPasswordException(
            IncorrectPasswordException e
    ) {
        Map<String, String> body = new HashMap<>();
        body.put("error", e.getMessage());
        return body;
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    @ResponseStatus(
            HttpStatus.NOT_FOUND
    )
    private Map<String, String> handleUserNotFoundException(
            UserNotFoundException e
    ) {
        Map<String, String> body = new HashMap<>();
        body.put("error", e.getMessage());
        return body;
    }
}