package com.randombank.onboarding;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends ListCrudRepository<User, UUID>, JpaSpecificationExecutor<User> {
    User findByUsername(String username);
}
