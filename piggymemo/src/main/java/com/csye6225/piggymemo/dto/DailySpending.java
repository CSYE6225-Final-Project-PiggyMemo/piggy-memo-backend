package com.csye6225.piggymemo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailySpending(LocalDate date, BigDecimal amount) {
}
