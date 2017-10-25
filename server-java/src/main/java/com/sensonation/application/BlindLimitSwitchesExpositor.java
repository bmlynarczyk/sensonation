package com.sensonation.application;

import com.sensonation.domain.BlindDriver;
import com.sensonation.domain.BlindLimitSwitchState;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.time.Clock;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.time.Instant.now;

@Slf4j
public class BlindLimitSwitchesExpositor {

    private static final Function<Map.Entry<String, BlindDriver>, BlindLimitSwitchState> PROVIDER_ENTRY_TRANSFORMER = entry -> {
        BlindDriver blindDriver = entry.getValue();
        logUnsuspectedState(blindDriver);
        return BlindLimitSwitchState.builder()
                .name(blindDriver.getName())
                .isPullDownLimitReached(blindDriver.isPullDownLimitReached())
                .isPullUpLimitReached(blindDriver.isPullUpLimitReached())
                .suspiciousStateReachingDate(Optional.empty())
                .build();
    };

    private final Clock clock;
    private final Map<String, BlindLimitSwitchState> states;
    private static final String IS = "is";
    private static final String IS_NOT = "isn't";

    public BlindLimitSwitchesExpositor(BlindDriversProvider blindDriversProvider, Clock clock) {
        this.clock = clock;
        states = Collections.synchronizedMap(blindDriversProvider.get().entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, PROVIDER_ENTRY_TRANSFORMER)));

    }

    public Set<BlindLimitSwitchState> getStates() {
        return states.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toSet());
    }

    public String getState(BlindDriver blindDriver) {
        return MessageFormat.format("in | {0} | pull down limit | {1} | reached, pull up limit | {2} | reached",
                blindDriver.getName(),
                blindDriver.isPullDownLimitReached() ? IS : IS_NOT,
                blindDriver.isPullUpLimitReached() ? IS : IS_NOT);
    }

    public void exposeState(BlindDriver blindDriver) {
        if(hasSuspiciousState(blindDriver)){
            handleSuspiciousState(blindDriver);
        } else {
            reportUnsuspectedState(blindDriver);
        }
    }

    private boolean hasSuspiciousState(BlindDriver blindDriver) {
        return blindDriver.isPullDownLimitReached() && blindDriver.isPullUpLimitReached();
    }

    private void handleSuspiciousState(BlindDriver blindDriver) {
        if(!isSuspiciousStateReported(blindDriver)){
            logSuspiciousState(blindDriver);
            reportSuspiciousState(blindDriver);
        }
    }

    private void logSuspiciousState(BlindDriver blindDriver) {
        log.info("[SUSPICIOUS STATE DETECTED!!!] pull down limit and pull up limit of blind {} are reached", blindDriver.getName());
    }

    private boolean isSuspiciousStateReported(BlindDriver blindDriver) {
        return states.get(blindDriver.getName()).getSuspiciousStateReachingDate().isPresent();
    }

    private void reportSuspiciousState(BlindDriver blindDriver) {
        BlindLimitSwitchState state = BlindLimitSwitchState.builder()
                .name(blindDriver.getName())
                .isPullDownLimitReached(blindDriver.isPullDownLimitReached())
                .isPullUpLimitReached(blindDriver.isPullUpLimitReached())
                .suspiciousStateReachingDate(Optional.of(now(clock)))
                .build();
        states.replace(blindDriver.getName(), state);
    }

    private void reportUnsuspectedState(BlindDriver blindDriver) {
        BlindLimitSwitchState state = BlindLimitSwitchState.builder()
                .name(blindDriver.getName())
                .isPullDownLimitReached(blindDriver.isPullDownLimitReached())
                .isPullUpLimitReached(blindDriver.isPullUpLimitReached())
                .suspiciousStateReachingDate(Optional.empty())
                .build();
        if(isSuspiciousStateReported(blindDriver))
            logUnsuspectedState(blindDriver);
        states.replace(blindDriver.getName(), state);
    }

    private static void logUnsuspectedState(BlindDriver blindDriver) {
        log.info("[unsuspected state]in blind driver {} pull down limit {} reached, pull up limit {} reached",
                blindDriver.getName(),
                blindDriver.isPullDownLimitReached() ? IS : IS_NOT,
                blindDriver.isPullUpLimitReached() ? IS : IS_NOT);
    }

}
