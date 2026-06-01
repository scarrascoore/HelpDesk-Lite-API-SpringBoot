package com.portfolio.helpdesk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IncidentCloseRequest(
        @NotBlank(message = "The solution is required")
        @Size(max = 800, message = "The solution must have a maximum of 800 characters")
        String solution
) {
}
