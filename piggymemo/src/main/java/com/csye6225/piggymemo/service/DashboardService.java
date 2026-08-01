package com.csye6225.piggymemo.service;

import com.csye6225.piggymemo.dto.BudgetExecution;
import com.csye6225.piggymemo.dto.DailySpending;
import com.csye6225.piggymemo.dto.OverviewDashboardResponse;
import com.csye6225.piggymemo.entity.PersonalBudgets;
import com.csye6225.piggymemo.repository.PersonalBudgetsRepository;
import com.csye6225.piggymemo.repository.SpendingsRepository;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class DashboardService {
    private static final ZoneId ZONE_ID = ZoneId.of("America/New_York");

    private final PersonalBudgetsRepository personalBudgetsRepository;
    private final SpendingsRepository spendingsRepository;

    public DashboardService(PersonalBudgetsRepository personalBudgetsRepository, SpendingsRepository spendingsRepository) {
        this.personalBudgetsRepository = personalBudgetsRepository;
        this.spendingsRepository = spendingsRepository;
    }

    public OverviewDashboardResponse getOverview(Long userId) {
        PersonalBudgets budget = personalBudgetsRepository.findByUser(userId).orElseGet(PersonalBudgets::new);
        BudgetExecution budgetExecution = new BudgetExecution(budget.getMonthlyBudget(), budget.getBudgetLeft());
        List<DailySpending> monthSpending = getCurrentMonthSpending(userId);
        return new OverviewDashboardResponse(budgetExecution, monthSpending);
    }

    private List<DailySpending> getCurrentMonthSpending(Long userId) {
        ZonedDateTime monthStart = ZonedDateTime.now(ZONE_ID).withDayOfMonth(1).toLocalDate().atStartOfDay(ZONE_ID);
        return spendingsRepository.findDailySpendingByMonth(userId, monthStart.toOffsetDateTime(), monthStart.plusMonths(1).toOffsetDateTime(), ZONE_ID.getId());
    }
}
