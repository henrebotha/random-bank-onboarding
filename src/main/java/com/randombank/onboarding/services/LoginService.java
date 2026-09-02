package com.randombank.onboarding.services;

import com.randombank.onboarding.User;

public interface LoginService {
    User login(String username, String password);
}
