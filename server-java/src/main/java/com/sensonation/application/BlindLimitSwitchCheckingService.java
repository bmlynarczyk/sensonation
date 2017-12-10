package com.sensonation.application;

import com.sensonation.domain.BlindDriver;
import com.sensonation.domain.BlindLimitSwitchState;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;

import java.time.Clock;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.time.Instant.now;

@Slf4j
public class BlindLimitSwitchCheckingService {

    private static final Function<Map.Entry<String, BlindDriver>, BlindLimitSwitchState> PROVIDER_ENTRY_TRANSFORMER = entry -> {
        BlindDriver blindDriver = entry.getValue();
        logState(blindDriver);
        return BlindLimitSwitchState.builder()
                .name(blindDriver.getName())
                .isPullDownLimitReached(blindDriver.isPullDownLimitReached())
                .isPullUpLimitReached(blindDriver.isPullUpLimitReached())
                .stateReachingDate(Optional.empty())
                .build();
    };
    private static final String IS = "is";
    private static final String IS_NOT = "isn't";

    private final Clock clock;
    private final Map<String, BlindLimitSwitchState> states;
    private final Collection<BlindDriver> blinds;

    public BlindLimitSwitchCheckingService(BlindDriversProvider blindDriversProvider, Clock clock, TaskExecutor taskExecutor) {
        this.clock = clock;
        this.blinds = Collections.synchronizedCollection(blindDriversProvider.get().values());
        states = new ConcurrentHashMap<>(blindDriversProvider.get().entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, PROVIDER_ENTRY_TRANSFORMER)));

        taskExecutor.execute(this::run);
    }

    Set<BlindLimitSwitchState> getStates() {
        return states.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toSet());
    }

    BlindLimitSwitchState getState(String blindName) {
        return states.get(blindName);
    }

    void reportState(BlindDriver blindDriver) {
        BlindLimitSwitchState previousState = states.get(blindDriver.getName());
        BlindLimitSwitchState actualState = createState(blindDriver);
        if (!previousState.equals(actualState)) {
            logState(blindDriver);
            states.replace(blindDriver.getName(), actualState);
        }
    }

    @SneakyThrows
    private void run() {
        while (true) {
            blinds.forEach(this::reportState);
            Thread.sleep(5);
        }
    }

    private BlindLimitSwitchState createState(BlindDriver blindDriver) {
        return BlindLimitSwitchState.builder()
                .name(blindDriver.getName())
                .isPullDownLimitReached(blindDriver.isPullDownLimitReached())
                .isPullUpLimitReached(blindDriver.isPullUpLimitReached())
                .stateReachingDate(Optional.of(now(clock)))
                .build();
    }

    private static void logState(BlindDriver blindDriver) {
        log.info("blind driver {} pull down limit {} reached, pull up limit {} reached",
                blindDriver.getName(),
                blindDriver.isPullDownLimitReached() ? IS : IS_NOT,
                blindDriver.isPullUpLimitReached() ? IS : IS_NOT);
    }

}