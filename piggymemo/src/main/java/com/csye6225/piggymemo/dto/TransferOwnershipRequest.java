package com.csye6225.piggymemo.dto;

import jakarta.validation.constraints.NotNull;

public class TransferOwnershipRequest {
    @NotNull(message = "New owner must be specified.")
    private Long newOwnerUserId;

    public TransferOwnershipRequest() {}

    public Long getNewOwnerUserId() {
        return newOwnerUserId;
    }

    public void setNewOwnerUserId(Long newOwnerUserId) {
        this.newOwnerUserId = newOwnerUserId;
    }
}
