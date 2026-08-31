package com.randombank.onboarding;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.userName = :userName")
    public User findByUserName(String userName);

    @Query("SELECT u FROM User u WHERE u.id = :id")
    public User getById(int id);
}