package com.sensonation.application;

import com.google.common.collect.ImmutableMap;
import com.sensonation.domain.BlindDriver;
import com.sensonation.domain.BlindEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class BlindActionsExecutor implements ActionsExecutor {

    private static final Consumer<BlindDriver> THROW_ILLEGAL_ACTION_EXCEPTION = blind -> {
        throw new IllegalArgumentException("unknown action");
    };

    private static final Function<String, BlindDriver> THROW_NO_BLIND_FOUND = key -> {
        throw new NoSuchElementException("unknown blind");
    };

    private final Map<String, BlindDriver> blinds;

    private final Map<String, Consumer<BlindDriver>> actions;

    public BlindActionsExecutor(BlindDriversProvider blindDriversProvider) {
        this.blinds = blindDriversProvider.get();
        this.actions = ImmutableMap.of(
                "pullDown", BlindDriver::pullDown,
                "pullUp", BlindDriver::pullUp,
                "stop", BlindDriver::stop
        );
    }

    public boolean shouldExecute(BlindEvent blindEvent) {
        return actions.keySet().contains(blindEvent.getActionName());
    }

    public void execute(BlindEvent blindEvent) {
        log.debug("execution of {} begin", blindEvent.getActionName());
        BlindDriver blindDriver = blinds.computeIfAbsent(blindEvent.getBlindName(), THROW_NO_BLIND_FOUND);
        actions.getOrDefault(blindEvent.getActionName(), THROW_ILLEGAL_ACTION_EXCEPTION).accept(blindDriver);
        log.debug("execution of {} done", blindEvent.getActionName());
    }

}
