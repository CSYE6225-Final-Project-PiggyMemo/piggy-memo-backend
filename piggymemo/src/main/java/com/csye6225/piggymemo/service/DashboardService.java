package com.csye6225.piggymemo.service;

import com.csye6225.piggymemo.dto.BudgetExecution;
import com.csye6225.piggymemo.dto.DailySpending;
import com.csye6225.piggymemo.dto.FamilyMemberDailySpending;
import com.csye6225.piggymemo.dto.OverviewDashboardResponse;
import com.csye6225.piggymemo.entity.FamilyBudgets;
import com.csye6225.piggymemo.entity.PersonalBudgets;
import com.csye6225.piggymemo.repository.FamilyBudgetsRepository;
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
    private final FamilyBudgetsRepository familyBudgetsRepository;
    private final SpendingsRepository spendingsRepository;
    private final ProfileService profileService;

    public DashboardService(
        PersonalBudgetsRepository personalBudgetsRepository,
        FamilyBudgetsRepository familyBudgetsRepository,
        SpendingsRepository spendingsRepository,
        ProfileService profileService
    ) {
        this.personalBudgetsRepository = personalBudgetsRepository;
        this.familyBudgetsRepository = familyBudgetsRepository;
        this.spendingsRepository = spendingsRepository;
        this.profileService = profileService;
    }

    public OverviewDashboardResponse getOverview(Long userId) {
        Long family = profileService.getProfileFamily(userId);

        ZonedDateTime monthStart = ZonedDateTime.now(ZONE_ID).withDayOfMonth(1).toLocalDate().atStartOfDay(ZONE_ID);
        ZonedDateTime nextMonthStart = monthStart.plusMonths(1);

        if (family == null) {
            PersonalBudgets budget = personalBudgetsRepository.findByUser(userId).orElseGet(PersonalBudgets::new);
            BudgetExecution budgetExecution = new BudgetExecution(budget.getMonthlyBudget(), budget.getBudgetLeft());
            List<DailySpending> monthSpending = spendingsRepository.findDailySpendingByMonth(
                userId, monthStart.toOffsetDateTime(), nextMonthStart.toOffsetDateTime(), ZONE_ID.getId()
            );
            return new OverviewDashboardResponse(budgetExecution, monthSpending, List.of());
        }

        FamilyBudgets budget = familyBudgetsRepository.findByFamily(family).orElseGet(FamilyBudgets::new);
        BudgetExecution budgetExecution = new BudgetExecution(budget.getMonthlyBudget(), budget.getBudgetLeft());
        List<DailySpending> monthSpending = spendingsRepository.findDailySpendingByMonthForFamily(
            family, monthStart.toOffsetDateTime(), nextMonthStart.toOffsetDateTime(), ZONE_ID.getId()
        );
        List<FamilyMemberDailySpending> memberSpending = spendingsRepository.findFamilyMemberDailySpendingByMonth(
            family, monthStart.toOffsetDateTime(), nextMonthStart.toOffsetDateTime(), ZONE_ID.getId()
        );
        return new OverviewDashboardResponse(budgetExecution, monthSpending, memberSpending);
    }
}
