package com.csye6225.piggymemo.service;

import java.util.List;

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
        return profileRepository.save(profile);
    }

    protected Long getProfileFamily(Long id) {
        Profile profile = profileRepository.findByUser(id).orElseThrow(() -> new RuntimeException("Profile not found"));
        return profile.getFamily();
    }

    //Family membership can only change via FamilyService's governed create/join/approve/leave/remove flows.
    protected Profile setProfileFamily(Long userId, Long familyId) {
        Profile profile = profileRepository.findByUser(userId).orElseThrow(() -> new RuntimeException("Profile not found"));
        profile.setFamily(familyId);
        return profileRepository.save(profile);
    }

    protected List<Profile> getFamilyMembers(Long familyId) {
        return profileRepository.findAllByFamily(familyId);
    }

    protected List<Profile> getProfilesByUsers(List<Long> userIds) {
        return profileRepository.findAllByUserIn(userIds);
    }

    private Profile createDefaultProfile(Long id, String username) {
        Profile profile = new Profile();
        profile.setUser(id);
        profile.setNickname(username);
        profile.setIsProfilePublic(true);
        return profileRepository.save(profile);
    }
}
