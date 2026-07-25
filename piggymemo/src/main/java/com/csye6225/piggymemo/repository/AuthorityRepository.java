package com.csye6225.piggymemo.repository;

import com.csye6225.piggymemo.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    List<Authority> findAllByUid(Long uid);
}
