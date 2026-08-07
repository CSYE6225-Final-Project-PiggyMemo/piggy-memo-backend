package com.csye6225.piggymemo.service;

import java.util.List;

import com.csye6225.piggymemo.dto.ProfileUpdateRequest;
import com.csye6225.piggymemo.entity.Profile;
import com.csye6225.piggymemo.entity.User;
import com.csye6225.piggymemo.repository.ProfileRepository;
import com.csye6225.piggymemo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
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

    // No profile row yet (e.g. a just-registered user who hasn't visited /profile,
    // which is what lazily creates it) simply means "not in a family" — never throw here.
    protected Long getProfileFamily(Long id) {
        return profileRepository.findByUser(id).map(Profile::getFamily).orElse(null);
    }

    //Family membership can only change via FamilyService's governed create/join/approve/leave/remove flows.
    @Transactional
    protected Profile setProfileFamily(Long userId, Long familyId) {
        Profile profile = getOrCreateProfile(userId);
        profile.setFamily(familyId);
        return profileRepository.save(profile);
    }

    protected Profile getOrCreateProfile(Long userId) {
        return profileRepository.findByUser(userId).orElseGet(() -> {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            return createDefaultProfile(userId, user.getUsername());
        });
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
