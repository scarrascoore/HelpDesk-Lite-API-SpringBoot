package com.portfolio.helpdesk.service;

import com.portfolio.helpdesk.dto.IncidentCloseRequest;
import com.portfolio.helpdesk.dto.IncidentCreateRequest;
import com.portfolio.helpdesk.dto.IncidentResponse;
import com.portfolio.helpdesk.dto.IncidentStatusUpdateRequest;
import com.portfolio.helpdesk.entity.Incident;
import com.portfolio.helpdesk.entity.IncidentPriority;
import com.portfolio.helpdesk.entity.IncidentStatus;
import com.portfolio.helpdesk.exception.IncidentAlreadyClosedException;
import com.portfolio.helpdesk.exception.IncidentNotFoundException;
import com.portfolio.helpdesk.repository.IncidentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidentServiceImpl implements IncidentService {
    private final IncidentRepository incidentRepository;

    public IncidentServiceImpl(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    @Override
    public List<IncidentResponse> findAll() {
        return incidentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public IncidentResponse findById(Long id) {
        Incident incident = findIncidentOrThrow(id);
        return toResponse(incident);
    }

    @Override
    public List<IncidentResponse> findByStatus(IncidentStatus status) {
        return incidentRepository.findByStatus(status)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<IncidentResponse> findByPriority(IncidentPriority priority) {
        return incidentRepository.findByPriority(priority)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<IncidentResponse> findByRequesterName(String requesterName) {
        return incidentRepository.findByRequesterNameContainingIgnoreCase(requesterName)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public IncidentResponse create(IncidentCreateRequest request) {
        Incident incident = new Incident(
                request.title(),
                request.description(),
                request.requesterName(),
                request.priority()
        );
        Incident savedIncident = incidentRepository.save(incident);
        return toResponse(savedIncident);
    }

    @Override
    public IncidentResponse updateStatus(Long id, IncidentStatusUpdateRequest request) {
        Incident incident = findIncidentOrThrow(id);
        validateIncidentIsNotClosed(incident);
        incident.updateStatus(request.status());
        Incident updatedIncident = incidentRepository.save(incident);
        return toResponse(updatedIncident);
    }

    @Override
    public IncidentResponse close(Long id, IncidentCloseRequest request) {
        Incident incident = findIncidentOrThrow(id);
        validateIncidentIsNotClosed(incident);
        incident.close(request.solution());
        Incident closedIncident = incidentRepository.save(incident);
        return toResponse(closedIncident);
    }

    @Override
    public void deleteById(Long id) {
        Incident incident = findIncidentOrThrow(id);
        incidentRepository.delete(incident);
    }

    private void validateIncidentIsNotClosed(Incident incident) {
        if (incident.isClosed()) {
            throw new IncidentAlreadyClosedException(incident.getId());
        }
    }

    private Incident findIncidentOrThrow(Long id) {
        return incidentRepository.findById(id)
                .orElseThrow(()->new IncidentNotFoundException(id));
    }

    private IncidentResponse toResponse(Incident incident) {
        return new IncidentResponse(
                incident.getId(),
                incident.getTitle(),
                incident.getDescription(),
                incident.getRequesterName(),
                incident.getPriority(),
                incident.getStatus(),
                incident.getSolution(),
                incident.getCreatedAt(),
                incident.getUpdatedAt(),
                incident.getClosedAt()
        );
    }




}
