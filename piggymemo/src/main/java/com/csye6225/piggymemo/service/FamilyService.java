package com.csye6225.piggymemo.service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.csye6225.piggymemo.dto.CreateFamilyRequest;
import com.csye6225.piggymemo.dto.FamilyDetailResponse;
import com.csye6225.piggymemo.dto.FamilyJoinRequestResponse;
import com.csye6225.piggymemo.dto.FamilyMemberResponse;
import com.csye6225.piggymemo.dto.FamilyResponse;
import com.csye6225.piggymemo.entity.Family;
import com.csye6225.piggymemo.entity.FamilyJoinRequest;
import com.csye6225.piggymemo.entity.Profile;
import com.csye6225.piggymemo.exception.AlreadyInFamilyException;
import com.csye6225.piggymemo.exception.FamilyMemberNotFoundException;
import com.csye6225.piggymemo.exception.FamilyNotFoundException;
import com.csye6225.piggymemo.exception.FamilyOwnerCannotLeaveException;
import com.csye6225.piggymemo.exception.FamilyPermissionDeniedException;
import com.csye6225.piggymemo.exception.InvalidFamilyCodeException;
import com.csye6225.piggymemo.exception.JoinRequestNotFoundException;
import com.csye6225.piggymemo.exception.NotInFamilyException;
import com.csye6225.piggymemo.repository.FamilyJoinRequestRepository;
import com.csye6225.piggymemo.repository.FamilyRepository;

//Every method derives "which family" from the caller's own profile - no endpoint may
//target a family by a client-supplied id, except the join code (which only creates a
//pending request, never grants access by itself).
@Service
public class FamilyService {
    private static final int CODE_LENGTH = 8;
    private static final String CODE_ALPHABET = "23456789ABCDEFGHJKMNPQRSTUVWXYZ"; // excludes 0/O/1/I/L

    private final FamilyRepository familyRepository;
    private final FamilyJoinRequestRepository familyJoinRequestRepository;
    private final ProfileService profileService;
    private final SecureRandom random = new SecureRandom();

    public FamilyService(
        FamilyRepository familyRepository,
        FamilyJoinRequestRepository familyJoinRequestRepository,
        ProfileService profileService
    ) {
        this.familyRepository = familyRepository;
        this.familyJoinRequestRepository = familyJoinRequestRepository;
        this.profileService = profileService;
    }

    @Transactional
    public FamilyResponse createFamily(Long ownerId, CreateFamilyRequest req) {
        if (profileService.getProfileFamily(ownerId) != null)
            throw new AlreadyInFamilyException("You are already in a family. Leave it first.");

        String requestedName = req == null ? null : req.getName();
        String familyName = (requestedName != null && !requestedName.isBlank())
            ? requestedName.trim() : "Happy Family";

        Family family = new Family();
        family.setOwnerUserId(ownerId);
        family.setFamilyCode(generateUniqueFamilyCode());
        family.setFamilyName(familyName);
        Family saved = familyRepository.save(family);

        profileService.setProfileFamily(ownerId, saved.getId());

        return new FamilyResponse(saved.getId(), saved.getFamilyCode(), saved.getFamilyName(), saved.getOwnerUserId(), saved.getCreatedAt());
    }

    @Transactional
    public void requestToJoin(Long userId, String familyCode) {
        if (profileService.getProfileFamily(userId) != null)
            throw new AlreadyInFamilyException("You are already in a family. Leave it first.");

        Family family = familyRepository.findByFamilyCode(normalizeCode(familyCode))
            .orElseThrow(() -> new InvalidFamilyCodeException("No family found for this code"));

        // Ensure the requester has a profile row now, so the owner's pending-requests
        // list can show a real nickname instead of a blank name (a brand-new user who
        // never visited /profile wouldn't have one otherwise).
        profileService.getOrCreateProfile(userId);

        familyJoinRequestRepository.deleteByUserId(userId); // at most one pending request per user

        FamilyJoinRequest joinRequest = new FamilyJoinRequest();
        joinRequest.setFamilyId(family.getId());
        joinRequest.setUserId(userId);
        familyJoinRequestRepository.save(joinRequest);
    }

    @Transactional
    public void cancelMyJoinRequest(Long userId) {
        familyJoinRequestRepository.deleteByUserId(userId);
    }

    @Transactional
    public List<FamilyJoinRequestResponse> listJoinRequests(Long ownerId) {
        Long family = requireFamily(ownerId);
        requireOwner(ownerId, family);

        List<FamilyJoinRequest> requests = familyJoinRequestRepository.findByFamilyIdOrderByCreatedAtAsc(family);
        Map<Long, Profile> profilesByUser = profileService
            .getProfilesByUsers(requests.stream().map(FamilyJoinRequest::getUserId).toList())
            .stream()
            .collect(Collectors.toMap(Profile::getUser, Function.identity()));

        return requests.stream()
            .map(jr -> {
                Profile requester = profilesByUser.get(jr.getUserId());
                String nickname = requester != null ? requester.getNickname() : null;
                return new FamilyJoinRequestResponse(jr.getId(), jr.getUserId(), nickname, jr.getCreatedAt());
            })
            .toList();
    }

    @Transactional
    public void approveJoinRequest(Long ownerId, Long requestId) {
        Long family = requireFamily(ownerId);
        requireOwner(ownerId, family);

        FamilyJoinRequest joinRequest = familyJoinRequestRepository.findByIdAndFamilyId(requestId, family)
            .orElseThrow(() -> new JoinRequestNotFoundException("Join request not found"));

        if (profileService.getProfileFamily(joinRequest.getUserId()) != null) {
            familyJoinRequestRepository.deleteById(requestId);
            throw new AlreadyInFamilyException("That user already joined a family.");
        }

        profileService.setProfileFamily(joinRequest.getUserId(), family);
        familyJoinRequestRepository.deleteByUserId(joinRequest.getUserId()); // purge any other pending requests
    }

    @Transactional
    public void rejectJoinRequest(Long ownerId, Long requestId) {
        Long family = requireFamily(ownerId);
        requireOwner(ownerId, family);

        familyJoinRequestRepository.findByIdAndFamilyId(requestId, family)
            .orElseThrow(() -> new JoinRequestNotFoundException("Join request not found"));

        familyJoinRequestRepository.deleteById(requestId);
    }

    //Returns null (not an error) when the caller isn't in a family - this endpoint doubles
    //as the frontend's "what is my family status" check.
    @Transactional
    public FamilyDetailResponse getFamilyDetail(Long userId) {
        Long family = profileService.getProfileFamily(userId);
        if (family == null) return null;

        Family f = familyRepository.findById(family)
            .orElseThrow(() -> new FamilyNotFoundException("Family not found"));

        List<FamilyMemberResponse> members = profileService.getFamilyMembers(family).stream()
            .map(p -> new FamilyMemberResponse(
                p.getUser(), p.getNickname(), p.getAvatarUrl(), p.getUser().equals(f.getOwnerUserId())
            ))
            .toList();

        return new FamilyDetailResponse(f.getId(), f.getFamilyCode(), f.getFamilyName(), f.getOwnerUserId(), f.getCreatedAt(), members);
    }

    @Transactional
    public void transferOwnership(Long ownerId, Long newOwnerUserId) {
        Long family = requireFamily(ownerId);
        requireOwner(ownerId, family);

        if (newOwnerUserId.equals(ownerId))
            throw new FamilyPermissionDeniedException("You are already the owner.");
        if (!family.equals(profileService.getProfileFamily(newOwnerUserId)))
            throw new FamilyMemberNotFoundException("That user is not a member of your family.");

        Family f = familyRepository.findById(family)
            .orElseThrow(() -> new FamilyNotFoundException("Family not found"));
        f.setOwnerUserId(newOwnerUserId);
        familyRepository.save(f);
    }

    @Transactional
    public void removeMember(Long ownerId, Long targetUserId) {
        Long family = requireFamily(ownerId);
        requireOwner(ownerId, family);

        if (targetUserId.equals(ownerId))
            throw new FamilyPermissionDeniedException("Use leave/transfer-ownership to remove yourself.");
        if (!family.equals(profileService.getProfileFamily(targetUserId)))
            throw new FamilyMemberNotFoundException("That user is not a member of your family.");

        profileService.setProfileFamily(targetUserId, null);
    }

    @Transactional
    public void leaveFamily(Long userId) {
        Long family = requireFamily(userId);

        if (isFamilyOwner(userId, family)) {
            boolean soleMember = profileService.getFamilyMembers(family).size() == 1;
            if (!soleMember)
                throw new FamilyOwnerCannotLeaveException(
                    "Transfer ownership to another member before leaving.");
            familyRepository.deleteById(family); // cascades family_budgets/family_join_requests
        }

        profileService.setProfileFamily(userId, null);
    }

    @Transactional
    public void deleteFamily(Long ownerId) {
        Long family = requireFamily(ownerId);
        requireOwner(ownerId, family);

        if (profileService.getFamilyMembers(family).size() != 1)
            throw new FamilyOwnerCannotLeaveException("Remove all other members before deleting the family.");

        familyRepository.deleteById(family);
        profileService.setProfileFamily(ownerId, null);
    }

    @Transactional
    public boolean isFamilyOwner(Long userId, Long familyId) {
        Family f = familyRepository.findById(familyId)
            .orElseThrow(() -> new FamilyNotFoundException("Family not found"));
        return f.getOwnerUserId().equals(userId);
    }

    private Long requireFamily(Long userId) {
        Long family = profileService.getProfileFamily(userId);
        if (family == null) throw new NotInFamilyException("You are not in a family.");
        return family;
    }

    private void requireOwner(Long userId, Long familyId) {
        if (!isFamilyOwner(userId, familyId))
            throw new FamilyPermissionDeniedException("Only the family owner can do this.");
    }

    private String normalizeCode(String code) {
        return code == null ? null : code.trim().toUpperCase();
    }

    private String generateUniqueFamilyCode() {
        String code;
        do {
            StringBuilder sb = new StringBuilder(CODE_LENGTH);
            for (int i = 0; i < CODE_LENGTH; i++) {
                sb.append(CODE_ALPHABET.charAt(random.nextInt(CODE_ALPHABET.length())));
            }
            code = sb.toString();
        } while (familyRepository.existsByFamilyCode(code));
        return code;
    }
}
