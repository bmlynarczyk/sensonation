package com.sensonation.representation;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.time.Instant;

@Data
@Builder
public class TaskRepresentation {

    private String name;
    private Instant executionDate;

}
