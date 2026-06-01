package com.portfolio.helpdesk.dto;

import com.portfolio.helpdesk.entity.IncidentPriority;
import com.portfolio.helpdesk.entity.IncidentStatus;

import java.time.LocalDateTime;

public record IncidentResponse(
        Long id,
        String title,
        String description,
        String requesterName,
        IncidentPriority priority,
        IncidentStatus status,
        String solution,
        LocalDateTime createdAt,
        LocalDateTime updateAt,
        LocalDateTime closeAt
) {
}
