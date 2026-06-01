package com.portfolio.helpdesk.repository;

import com.portfolio.helpdesk.entity.Incident;
import com.portfolio.helpdesk.entity.IncidentPriority;
import com.portfolio.helpdesk.entity.IncidentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByStatus(IncidentStatus status);
    List<Incident> findByPriority(IncidentPriority priority);
    List<Incident> findByRequesterNameContainingIgnoreCase(String requesterName);

}
