package com.sensonation.application;

import com.google.common.collect.ImmutableMap;
import com.sensonation.domain.BlindDriver;
import com.sensonation.domain.BlindEvent;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;

public class BlindActionsExecutor {

    private static final Consumer<BlindDriver> THROW_ILLEGAL_ACTION_EXCEPTION = blind -> {
        throw new IllegalArgumentException("unknown action");
    };

    private static final Function<String, BlindDriver> THROW_NO_BLIND_FOUND = key -> {
        throw new NoSuchElementException("unknown blind");
    };

    private final Map<String, BlindDriver> blinds;

    private final Map<String, Consumer<BlindDriver>> actions;

    public BlindActionsExecutor(BlindDriversProvider blindsProvider) {
        this.blinds = blindsProvider.get();
        this.actions = ImmutableMap.of(
                "pullDown", BlindDriver::pullDown,
                "pullUp", BlindDriver::pullUp,
                "stop", BlindDriver::stop
        );
    }

    boolean shouldExecute(BlindEvent blindEvent) {
        return actions.keySet().contains(blindEvent.getActionName());
    }

    void execute(BlindEvent blindEvent) {
        BlindDriver blindDriver = blinds.computeIfAbsent(blindEvent.getBlindName(), THROW_NO_BLIND_FOUND);
        actions.getOrDefault(blindEvent.getActionName(), THROW_ILLEGAL_ACTION_EXCEPTION).accept(blindDriver);
    }

    void stopAll() {
        blinds.values().forEach(BlindDriver::stop);
    }
}
