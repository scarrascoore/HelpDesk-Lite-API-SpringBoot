package com.portfolio.helpdesk.dto;

import com.portfolio.helpdesk.entity.IncidentPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record IncidentCreateRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 150, message = "The title must have a maximum of 150 characters")
        String title,

        @NotBlank(message = "The description is required")
        @Size(max = 500, message = "The description must have a maximum of 500 characters")
        String description,

        @NotBlank(message = "The requester name is required")
        @Size(max = 100, message = "The requester name must have a maximum of 100 characters")
        String requesterName,

        @NotNull(message = "The priority is required")
        IncidentPriority priority
) {
}
