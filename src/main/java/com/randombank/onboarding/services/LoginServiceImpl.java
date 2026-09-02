package com.randombank.onboarding.services;

import com.randombank.onboarding.User;
import com.randombank.onboarding.exceptions.IncorrectPasswordException;
import com.randombank.onboarding.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    private final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private final UserService userService;

    public LoginServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User login(String username, String password) {
        User user = userService.findByUsername(username);

        if (user == null) {
            throw new UserNotFoundException();
        }

        if (!user.password().equals(password)) {
            throw new IncorrectPasswordException();
        }

        return user;
    }
}