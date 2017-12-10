package com.sensonation.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.Instant;
import java.util.Optional;

@Value
@Builder
@EqualsAndHashCode(exclude = {"suspiciousStateReachingDate", "stateReachingDate"})
public class BlindLimitSwitchState {

    private final String name;
    private final boolean isPullDownLimitReached;
    private final boolean isPullUpLimitReached;
    private final Optional<Instant> stateReachingDate;

    public boolean isSuspiciousStateReported() {
        return isPullDownLimitReached && isPullUpLimitReached;
    }

}
