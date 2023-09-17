package com.example.consumer.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import  java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor

public class IncidentDTO {

    private Long id;
    private Long powerPlantId;
    private OffsetDateTime timestamp;
    private String description;
    private String severity;
    private String status;
    private User reportedBy;
    private User assignedTo;
    private String resolution;
    private OffsetDateTime resolvedAt;
    private PowerPlant powerPlant;

}
