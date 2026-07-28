package com.csye6225.piggymemo.service;

import com.csye6225.piggymemo.dto.ProfileUpdateRequest;
import com.csye6225.piggymemo.entity.Profile;
import com.csye6225.piggymemo.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Transactional
    public Profile getProfile(Long id, String username) {
        return profileRepository.findByUser(id).orElseGet(() -> createDefaultProfile(id, username));
    }

    @Transactional
    public Profile updateProfile(Long id, ProfileUpdateRequest request) {
        Profile profile = profileRepository.findByUser(id).orElseThrow(() -> new RuntimeException("Profile not found"));
        profile.setAvatarUrl(request.avatarUrl());
        profile.setNickname(request.nickname());
        profile.setBio(request.bio());
        profile.setIsProfilePublic(request.isProfilePublic());
        profile.setFamily(request.family());
        return profileRepository.save(profile);
    }

    private Profile createDefaultProfile(Long id, String username) {
        Profile profile = new Profile();
        profile.setUser(id);
        profile.setNickname(username);
        profile.setIsProfilePublic(true);
        return profileRepository.save(profile);
    }
}
