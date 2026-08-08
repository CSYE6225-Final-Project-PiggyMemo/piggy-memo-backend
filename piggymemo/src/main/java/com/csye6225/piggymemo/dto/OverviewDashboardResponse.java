package com.csye6225.piggymemo.dto;

import java.util.List;

public record OverviewDashboardResponse(
    BudgetExecution budgetExecution,
    List<DailySpending> monthlySpending,
    List<FamilyMemberDailySpending> memberSpending
) {
}
