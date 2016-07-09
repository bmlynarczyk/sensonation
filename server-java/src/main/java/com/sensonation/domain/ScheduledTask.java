package com.sensonation.domain;

import lombok.Value;

import java.time.Instant;
import java.util.concurrent.ScheduledFuture;

@Value
public class ScheduledTask {

    Instant executionDate;
    ScheduledFuture<?> scheduledFuture;

}
