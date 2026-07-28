package com.csye6225.piggymemo.repository;

import com.csye6225.piggymemo.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUser(Long user);
}
