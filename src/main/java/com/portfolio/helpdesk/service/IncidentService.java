package com.portfolio.helpdesk.service;

import com.portfolio.helpdesk.dto.IncidentCloseRequest;
import com.portfolio.helpdesk.dto.IncidentCreateRequest;
import com.portfolio.helpdesk.dto.IncidentResponse;
import com.portfolio.helpdesk.dto.IncidentStatusUpdateRequest;
import com.portfolio.helpdesk.entity.IncidentPriority;
import com.portfolio.helpdesk.entity.IncidentStatus;

import java.util.List;

public interface IncidentService {
    List<IncidentResponse> findAll();
    IncidentResponse findById(Long id);
    List<IncidentResponse> findByStatus(IncidentStatus status);
    List<IncidentResponse> findByPriority(IncidentPriority priority);
    List<IncidentResponse> findByRequesterName(String requesterName);
    IncidentResponse create(IncidentCreateRequest request);
    IncidentResponse updateStatus(Long id, IncidentStatusUpdateRequest request);
    IncidentResponse close(Long id, IncidentCloseRequest request);
    void deleteById(Long id);
}
