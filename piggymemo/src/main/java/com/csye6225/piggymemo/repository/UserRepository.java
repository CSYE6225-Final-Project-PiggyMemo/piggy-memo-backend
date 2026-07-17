package com.csye6225.piggymemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csye6225.piggymemo.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    public boolean existsByUsername(String username);
}
