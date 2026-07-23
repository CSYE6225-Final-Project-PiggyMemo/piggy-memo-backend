package com.csye6225.piggymemo.repository;

import com.csye6225.piggymemo.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByUid(Long id);
}
