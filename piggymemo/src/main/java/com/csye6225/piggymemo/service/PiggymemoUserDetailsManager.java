package com.csye6225.piggymemo.service;

import com.csye6225.piggymemo.entity.Authority;
import com.csye6225.piggymemo.entity.User;
import com.csye6225.piggymemo.exception.UsernameAlreadyExistsException;
import com.csye6225.piggymemo.repository.AuthorityRepository;
import com.csye6225.piggymemo.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Security adaptor
 */
@Service
public class PiggymemoUserDetailsManager implements UserDetailsManager {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    public PiggymemoUserDetailsManager(UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));
        String[] authorities = (String[]) authorityRepository.findAllByUid(user.getId())
            .stream()
            .map(Authority::getIdentity)
            .toArray();
        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .authorities(authorities)
            .build();
    }

    @Override
    @Transactional
    public void createUser(UserDetails userDetails) {
        doCreateUser(userDetails);
    }

    @Transactional
    public User createUserAndReturn(UserDetails userDetails) {
        return doCreateUser(userDetails);
    }

    private User doCreateUser(UserDetails userDetails) {
        if (userExists(userDetails.getUsername())) {
            throw new UsernameAlreadyExistsException("Username " + userDetails.getUsername() + " already exists!");
        }
        User user = new User();
        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        User savedUser = userRepository.save(user);
        List<Authority> authorities = userDetails.getAuthorities().stream()
            .map(grantedAuthority -> {
                Authority authority = new Authority();
                authority.setUid(savedUser.getId());
                authority.setIdentity(grantedAuthority.getAuthority());
                return authority;
            })
            .toList();
        authorityRepository.saveAll(authorities);
        return savedUser;
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public void updateUser(UserDetails user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteUser(String username) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new UnsupportedOperationException();
    }
}
