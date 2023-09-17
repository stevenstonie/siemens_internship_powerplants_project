package com.example.consumer.model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.OffsetDateTime;
@NoArgsConstructor
@Getter
@Setter
public class Incident implements Serializable {

    private Long id;
    private OffsetDateTime timestamp;
    private String description;
    private String severity;
    private String status;
    private String resolution;
    private OffsetDateTime resolvedAt;
    private User reportedBy;
    private User assignedTo;
    private PowerPlant powerPlant;

}
