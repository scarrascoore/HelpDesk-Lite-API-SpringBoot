package com.portfolio.helpdesk.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="incidents")
public class Incident {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 150)
    private String title;

    @Column(nullable = false,length = 800)
    private String description;

    @Column(nullable = false,length = 100)
    private String requesterName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private IncidentPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 30)
    private IncidentStatus status;

    @Column(length = 800)
    private String solution;

    @Column(nullable = false)
    private LocalDateTime created_at;

    private LocalDateTime updated_at;
    private LocalDateTime closed_at;

    protected Incident() {}

    public Incident(
            String title,
            String description,
            String requester_name,
            IncidentPriority priority
    ){
        this.title = title;
        this.description = description;
        this.requesterName = requester_name;
        this.priority = priority;
        this.status = IncidentStatus.OPEN;
        this.created_at = LocalDateTime.now();
    }

    public void updateStatus(IncidentStatus status){
        this.status = status;
        this.updated_at = LocalDateTime.now();
    }

    public void close(String solution){
        this.status = IncidentStatus.CLOSED;
        this.solution = solution;
        this.closed_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }

    public boolean isClosed(){
        return this.status == IncidentStatus.CLOSED;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getRequester_name() {
        return requesterName;
    }

    public IncidentPriority getPriority() {
        return priority;
    }

    public IncidentStatus getStatus() {
        return status;
    }

    public String getSolution() {
        return solution;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public LocalDateTime getClosed_at() {
        return closed_at;
    }
}
