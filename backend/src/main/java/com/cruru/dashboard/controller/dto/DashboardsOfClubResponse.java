package com.cruru.dashboard.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record DashboardsOfClubResponse(

        String clubName,

        @JsonProperty(value = "dashboards")
        List<DashboardPreviewResponse> dashboardPreviewResponses
) {

}
