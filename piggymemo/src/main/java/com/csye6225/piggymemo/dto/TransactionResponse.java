package com.csye6225.piggymemo.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TransactionResponse(
    BigDecimal amount, BigDecimal budgetLeftNow, String category, String notes, OffsetDateTime time
) {}
