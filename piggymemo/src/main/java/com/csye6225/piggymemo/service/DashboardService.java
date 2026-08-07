package com.csye6225.piggymemo.service;

import com.csye6225.piggymemo.dto.BudgetExecution;
import com.csye6225.piggymemo.dto.BudgetResponse;
import com.csye6225.piggymemo.dto.DailySpending;
import com.csye6225.piggymemo.dto.FamilyMemberDailySpending;
import com.csye6225.piggymemo.dto.OverviewDashboardResponse;
import com.csye6225.piggymemo.repository.SpendingsRepository;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class DashboardService {
    private static final ZoneId ZONE_ID = ZoneId.of("America/New_York");

    private final SpendingsRepository spendingsRepository;
    private final ProfileService profileService;
    private final BudgetService budgetService;

    public DashboardService(
        SpendingsRepository spendingsRepository,
        ProfileService profileService,
        BudgetService budgetService
    ) {
        this.spendingsRepository = spendingsRepository;
        this.profileService = profileService;
        this.budgetService = budgetService;
    }

    public OverviewDashboardResponse getOverview(Long userId) {
        Long family = profileService.getProfileFamily(userId);

        // Routing through BudgetService (instead of querying the budget repositories
        // directly) means the dashboard always reflects the same rolled-over period /
        // reset budgetLeft that BudgetService.getBudget() would return.
        BudgetResponse budgetResponse = budgetService.getBudget(userId);
        BudgetExecution budgetExecution = new BudgetExecution(budgetResponse.currentBudget(), budgetResponse.budgetLeft());

        ZonedDateTime monthStart = ZonedDateTime.now(ZONE_ID).withDayOfMonth(1).toLocalDate().atStartOfDay(ZONE_ID);
        ZonedDateTime nextMonthStart = monthStart.plusMonths(1);

        if (family == null) {
            List<DailySpending> monthSpending = spendingsRepository.findDailySpendingByMonth(
                userId, monthStart.toOffsetDateTime(), nextMonthStart.toOffsetDateTime(), ZONE_ID.getId()
            );
            return new OverviewDashboardResponse(budgetExecution, monthSpending, List.of());
        }

        List<DailySpending> monthSpending = spendingsRepository.findDailySpendingByMonthForFamily(
            family, monthStart.toOffsetDateTime(), nextMonthStart.toOffsetDateTime(), ZONE_ID.getId()
        );
        List<FamilyMemberDailySpending> memberSpending = spendingsRepository.findFamilyMemberDailySpendingByMonth(
            family, monthStart.toOffsetDateTime(), nextMonthStart.toOffsetDateTime(), ZONE_ID.getId()
        );
        return new OverviewDashboardResponse(budgetExecution, monthSpending, memberSpending);
    }
}
