package com.sensonation.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

public interface BlindSchedulerPolicy {
    Optional<Instant> getPullUpDateTime();
    Optional<Instant> getPullDownDateTime();
}
