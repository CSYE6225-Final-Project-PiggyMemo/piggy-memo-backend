package com.csye6225.piggymemo.controller;

import com.csye6225.piggymemo.dto.OverviewDashboardResponse;
import com.csye6225.piggymemo.security.CurrentUser;
import com.csye6225.piggymemo.service.DashboardService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/overview")
    public OverviewDashboardResponse getOverview(@AuthenticationPrincipal CurrentUser user) {
        return dashboardService.getOverview(user.id());
    }
}
