package com.csye6225.piggymemo.service;

import com.csye6225.piggymemo.config.AdminAccountProperties;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AdminAccountInitializer implements ApplicationRunner {
    private static final String ADMIN_AUTHORITY = "ADMIN";
    private static final String DEFAULT_AUTHORITY = "USER";

    private final PiggymemoUserDetailsManager piggymemoUserDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final AdminAccountProperties adminAccountProperties;

    public AdminAccountInitializer(
        PiggymemoUserDetailsManager piggymemoUserDetailsManager,
        PasswordEncoder passwordEncoder,
        AdminAccountProperties adminAccountProperties
    ) {
        this.piggymemoUserDetailsManager = piggymemoUserDetailsManager;
        this.passwordEncoder = passwordEncoder;
        this.adminAccountProperties = adminAccountProperties;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        adminAccountProperties.getAccounts()
            .forEach(account -> createAdminIfAbsent(account.getUsername(), account.getPassword()));
    }

    private void createAdminIfAbsent(String username, String rawPassword) {
        if (piggymemoUserDetailsManager.userExists(username)) {
            return;
        }
        UserDetails userDetails = org.springframework.security.core.userdetails.User
            .withUsername(username)
            .password(passwordEncoder.encode(rawPassword))
            .authorities(ADMIN_AUTHORITY, DEFAULT_AUTHORITY)
            .build();
        piggymemoUserDetailsManager.createUser(userDetails);
    }
}
