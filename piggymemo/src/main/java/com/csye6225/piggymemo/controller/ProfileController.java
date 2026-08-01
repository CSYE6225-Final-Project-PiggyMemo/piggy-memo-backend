package com.csye6225.piggymemo.controller;

import com.csye6225.piggymemo.dto.ProfileUpdateRequest;
import com.csye6225.piggymemo.entity.Profile;
import com.csye6225.piggymemo.security.CurrentUser;
import com.csye6225.piggymemo.service.ProfileService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public Profile getProfile(@AuthenticationPrincipal CurrentUser currentUser) {
        return profileService.getProfile(currentUser.id(), currentUser.username());
    }

    @PostMapping("/edit")
    public Profile updateProfile(@AuthenticationPrincipal CurrentUser currentUser, @RequestBody ProfileUpdateRequest request) {
        return profileService.updateProfile(currentUser.id(), request);
    }
}
