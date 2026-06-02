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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncidentServiceImplTest {

    @Mock
    private IncidentRepository incidentRepository;

    @InjectMocks
    private IncidentServiceImpl incidentService;

    @Test
    void shouldCreateIncidentSuccessfully() {
        IncidentCreateRequest request = new IncidentCreateRequest(
                "Error al iniciar sesión",
                "El usuario no puede ingresar al sistema",
                "Usuario de prueba",
                IncidentPriority.HIGH
        );

        Incident savedIncident = new Incident(
                request.title(),
                request.description(),
                request.requesterName(),
                request.priority()
        );
        setId(savedIncident, 1L);

        when(incidentRepository.save(any(Incident.class))).thenReturn(savedIncident);

        IncidentResponse response = incidentService.create(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Error al iniciar sesión", response.title());
        assertEquals(IncidentPriority.HIGH, response.priority());
        assertEquals(IncidentStatus.OPEN, response.status());

        verify(incidentRepository, times(1)).save(any(Incident.class));
    }

    @Test
    void shouldFindIncidentByIdSuccessfully() {
        Incident incident = new Incident(
                "Error de impresora",
                "La impresora no responde",
                "Área administrativa",
                IncidentPriority.MEDIUM
        );
        setId(incident, 1L);

        when(incidentRepository.findById(1L)).thenReturn(Optional.of(incident));

        IncidentResponse response = incidentService.findById(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Error de impresora", response.title());
        assertEquals(IncidentStatus.OPEN, response.status());

        verify(incidentRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenIncidentDoesNotExist() {
        when(incidentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                IncidentNotFoundException.class,
                () -> incidentService.findById(99L)
        );

        verify(incidentRepository, times(1)).findById(99L);
    }

    @Test
    void shouldUpdateIncidentStatusSuccessfully() {
        Incident incident = new Incident(
                "Error en sistema",
                "El sistema muestra pantalla en blanco",
                "Usuario de prueba",
                IncidentPriority.HIGH
        );
        setId(incident, 1L);

        IncidentStatusUpdateRequest request = new IncidentStatusUpdateRequest(
                IncidentStatus.IN_PROGRESS
        );

        when(incidentRepository.findById(1L)).thenReturn(Optional.of(incident));
        when(incidentRepository.save(any(Incident.class))).thenReturn(incident);

        IncidentResponse response = incidentService.updateStatus(1L, request);

        assertNotNull(response);
        assertEquals(IncidentStatus.IN_PROGRESS, response.status());

        verify(incidentRepository, times(1)).findById(1L);
        verify(incidentRepository, times(1)).save(incident);
    }

    @Test
    void shouldCloseIncidentSuccessfully() {
        Incident incident = new Incident(
                "Error de acceso",
                "El usuario no puede acceder al módulo de reportes",
                "Usuario de prueba",
                IncidentPriority.MEDIUM
        );
        setId(incident, 1L);

        IncidentCloseRequest request = new IncidentCloseRequest(
                "Se asignaron permisos al usuario y se verificó el acceso."
        );

        when(incidentRepository.findById(1L)).thenReturn(Optional.of(incident));
        when(incidentRepository.save(any(Incident.class))).thenReturn(incident);

        IncidentResponse response = incidentService.close(1L, request);

        assertNotNull(response);
        assertEquals(IncidentStatus.CLOSED, response.status());
        assertEquals("Se asignaron permisos al usuario y se verificó el acceso.", response.solution());
        assertNotNull(response.closeAt());

        verify(incidentRepository, times(1)).findById(1L);
        verify(incidentRepository, times(1)).save(incident);
    }

    @Test
    void shouldThrowExceptionWhenClosingAlreadyClosedIncident() {
        Incident incident = new Incident(
                "Error resuelto",
                "Incidencia previamente cerrada",
                "Usuario de prueba",
                IncidentPriority.LOW
        );
        setId(incident, 1L);
        incident.close("Solución registrada previamente.");

        IncidentCloseRequest request = new IncidentCloseRequest(
                "Nueva solución no permitida."
        );

        when(incidentRepository.findById(1L)).thenReturn(Optional.of(incident));

        assertThrows(
                IncidentAlreadyClosedException.class,
                () -> incidentService.close(1L, request)
        );

        verify(incidentRepository, times(1)).findById(1L);
        verify(incidentRepository, never()).save(any(Incident.class));
    }

    private void setId(Incident incident, Long id) {
        try {
            Field idField = Incident.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(incident, id);
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            throw new RuntimeException("Could not set incident id for test", exception);
        }
    }
}