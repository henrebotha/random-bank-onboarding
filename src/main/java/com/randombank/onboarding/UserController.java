package com.randombank.onboarding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/register")
    public User register(
            @RequestBody CreateUserDTO user
    ) {
        // Validate:
        //   username must be unique
        //   address must be NL/BE
        //   age must be >= 18
        // Generate:
        //   IBAN per NL format
        //   default password

        logger.info("/register request: {}", user.toString());
        return userService.createUser(user);
    }

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    private Map<String, String> handleUserAlreadyExistsException(
            UserAlreadyExistsException e
    ) {
        Map<String, String> body = new HashMap<>();
        body.put("error", e.getMessage());
        return body;
    }

    @ExceptionHandler(value = UserCountryInvalidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private Map<String, String> handleUserCountryInvalidException(
            UserCountryInvalidException e
    ) {
        Map<String, String> body = new HashMap<>();
        body.put("error", e.getMessage());
        return body;
    }

    @ExceptionHandler(value = UserTooYoungException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private Map<String, String> handleUserTooYoungException(
            UserTooYoungException e
    ) {
        Map<String, String> body = new HashMap<>();
        body.put("error", e.getMessage());
        return body;
    }

    @GetMapping("/overview/{id}")
    public User overview(@PathVariable String id) {
        logger.info("/overview request: {}", id);
        return userService.findUserById(id);
    }

    @GetMapping("/login")
    public User login() {
        logger.info("/login request");
        // return userService.login();
        return null;
    }
}