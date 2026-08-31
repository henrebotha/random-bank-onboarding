package com.randombank.onboarding;

import java.util.List;

public interface UserService {
    User createUser(CreateUserDTO user);

    List<User> findAllUsers();

    User findUserById(String id);

    User findUserByUsername(String username);

    void deleteAllUsers();
}
