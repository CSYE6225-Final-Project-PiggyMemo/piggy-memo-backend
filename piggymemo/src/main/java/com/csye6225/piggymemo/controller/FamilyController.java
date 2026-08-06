package com.csye6225.piggymemo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csye6225.piggymemo.dto.CreateFamilyRequest;
import com.csye6225.piggymemo.dto.FamilyDetailResponse;
import com.csye6225.piggymemo.dto.FamilyJoinRequestResponse;
import com.csye6225.piggymemo.dto.FamilyResponse;
import com.csye6225.piggymemo.dto.JoinFamilyRequest;
import com.csye6225.piggymemo.dto.TransferOwnershipRequest;
import com.csye6225.piggymemo.security.CurrentUser;
import com.csye6225.piggymemo.service.FamilyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/family")
public class FamilyController {
    private final FamilyService familyService;

    public FamilyController(FamilyService familyService) {
        this.familyService = familyService;
    }

    @PostMapping("/create")
    public FamilyResponse createFamily(@AuthenticationPrincipal CurrentUser user, @RequestBody(required = false) CreateFamilyRequest req) {
        return familyService.createFamily(user.id(), req);
    }

    @PostMapping("/join")
    public ResponseEntity<Void> joinFamily(@AuthenticationPrincipal CurrentUser user, @Valid @RequestBody JoinFamilyRequest req) {
        familyService.requestToJoin(user.id(), req.getFamilyCode());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping("/join/cancel")
    public ResponseEntity<Void> cancelJoinRequest(@AuthenticationPrincipal CurrentUser user) {
        familyService.cancelMyJoinRequest(user.id());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/join-requests")
    public List<FamilyJoinRequestResponse> listJoinRequests(@AuthenticationPrincipal CurrentUser user) {
        return familyService.listJoinRequests(user.id());
    }

    @PostMapping("/join-requests/{requestId}/approve")
    public ResponseEntity<Void> approveJoinRequest(@AuthenticationPrincipal CurrentUser user, @PathVariable Long requestId) {
        familyService.approveJoinRequest(user.id(), requestId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/join-requests/{requestId}/reject")
    public ResponseEntity<Void> rejectJoinRequest(@AuthenticationPrincipal CurrentUser user, @PathVariable Long requestId) {
        familyService.rejectJoinRequest(user.id(), requestId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public FamilyDetailResponse getFamily(@AuthenticationPrincipal CurrentUser user) {
        return familyService.getFamilyDetail(user.id());
    }

    @PostMapping("/transfer-ownership")
    public ResponseEntity<Void> transferOwnership(@AuthenticationPrincipal CurrentUser user, @Valid @RequestBody TransferOwnershipRequest req) {
        familyService.transferOwnership(user.id(), req.getNewOwnerUserId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/members/{userId}/remove")
    public ResponseEntity<Void> removeMember(@AuthenticationPrincipal CurrentUser user, @PathVariable Long userId) {
        familyService.removeMember(user.id(), userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/leave")
    public ResponseEntity<Void> leaveFamily(@AuthenticationPrincipal CurrentUser user) {
        familyService.leaveFamily(user.id());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFamily(@AuthenticationPrincipal CurrentUser user) {
        familyService.deleteFamily(user.id());
        return ResponseEntity.noContent().build();
    }
}
