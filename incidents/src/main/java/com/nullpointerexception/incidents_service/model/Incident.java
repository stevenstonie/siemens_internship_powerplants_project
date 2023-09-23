package com.nullpointerexception.incidents_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private OffsetDateTime timestamp;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String severity;

    @Column(nullable = false)
    private String status;
    @Column
    private String resolution;

    @Column
    private OffsetDateTime resolvedAt;

    @OneToOne
    @JoinColumn(name = "reported_by")
    private User reportedBy;

    @OneToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;


    @ManyToOne
    @JoinColumn(name = "power_plant_id")
    private PowerPlant powerPlant;

}
