package com.sensonation.domain;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.Optional;

@Value
@Builder
public class BlindLimitSwitchState {

    private final String name;
    private final boolean isPullDownLimitReached;
    private final boolean isPullUpLimitReached;
    private final Optional<Instant> suspiciousStateReachingDate;

}
