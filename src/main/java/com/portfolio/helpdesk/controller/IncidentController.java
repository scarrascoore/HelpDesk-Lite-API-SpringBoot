package com.portfolio.helpdesk.controller;

import com.portfolio.helpdesk.dto.IncidentCloseRequest;
import com.portfolio.helpdesk.dto.IncidentCreateRequest;
import com.portfolio.helpdesk.dto.IncidentResponse;
import com.portfolio.helpdesk.dto.IncidentStatusUpdateRequest;
import com.portfolio.helpdesk.entity.Incident;
import com.portfolio.helpdesk.entity.IncidentPriority;
import com.portfolio.helpdesk.entity.IncidentStatus;
import com.portfolio.helpdesk.service.IncidentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentService incidentService;
    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping
    public ResponseEntity<List<IncidentResponse>> findAll() {
        return ResponseEntity.ok(incidentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.findById(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<IncidentResponse>> findByStatus(
            @PathVariable IncidentStatus status)
    {
        return ResponseEntity.ok(incidentService.findByStatus(status));
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<IncidentResponse>> findByPriority(
            @PathVariable IncidentPriority priority
    ){
        return ResponseEntity.ok(incidentService.findByPriority(priority));
    }

    @GetMapping("/requester")
    public ResponseEntity<List<IncidentResponse>> findByRequesterName(
            @RequestParam String name)
    {
        return ResponseEntity.ok(incidentService.findByRequesterName(name));
    }

    @PostMapping
    public ResponseEntity<IncidentResponse> create(
            @Valid @RequestBody IncidentCreateRequest request)
    {
        IncidentResponse response = incidentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<IncidentResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody IncidentStatusUpdateRequest request)
    {
        return ResponseEntity.ok(incidentService.updateStatus(id, request));
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<IncidentResponse> close(
            @PathVariable Long id,
            @Valid @RequestBody IncidentCloseRequest request)
    {
        return ResponseEntity.ok(incidentService.close(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id)
    {
        incidentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
