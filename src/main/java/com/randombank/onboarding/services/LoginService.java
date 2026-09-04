package com.randombank.onboarding.services;

import com.randombank.onboarding.User;
import jakarta.servlet.http.HttpSession;

public interface LoginService {
    User login(HttpSession session, String username, String password);
}
