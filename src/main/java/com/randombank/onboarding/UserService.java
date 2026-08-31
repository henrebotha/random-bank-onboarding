package com.randombank.onboarding;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(CreateUserDTO user);

    List<User> findAll();

    User findById(UUID id);

    User findByUsername(String username);
}
