package com.portfolio.helpdesk.dto;

import com.portfolio.helpdesk.entity.IncidentStatus;
import jakarta.validation.constraints.NotNull;

public record IncidentStatusUpdateRequest(
        @NotNull(message = "The status is required")
        IncidentStatus status
) {
}
