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
        if(request.avatarUrl() != null)
            profile.setAvatarUrl(request.avatarUrl());
        if(request.nickname() != null)
            profile.setNickname(request.nickname());
        if(request.bio() != null)
            profile.setBio(request.bio());
        if(request.isProfilePublic() != null)
            profile.setIsProfilePublic(request.isProfilePublic());
        if(request.family() != null)
            profile.setFamily(request.family());
        return profileRepository.save(profile);
    }

    protected Long getProfileFamily(Long id) {
        Profile profile = profileRepository.findByUser(id).orElseThrow(() -> new RuntimeException("Profile not found"));
        return profile.getFamily();
    }

    private Profile createDefaultProfile(Long id, String username) {
        Profile profile = new Profile();
        profile.setUser(id);
        profile.setNickname(username);
        profile.setIsProfilePublic(true);
        return profileRepository.save(profile);
    }
}
