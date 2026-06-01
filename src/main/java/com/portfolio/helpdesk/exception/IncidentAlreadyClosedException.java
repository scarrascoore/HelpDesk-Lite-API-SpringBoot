package com.portfolio.helpdesk.exception;

public class IncidentAlreadyClosedException extends RuntimeException {
    public IncidentAlreadyClosedException(Long id) {
      super("Incident with id " + id + " is already closed");
    }
}
