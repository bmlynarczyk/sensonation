package com.sensonation.domain;

import com.sensonation.application.BlindDriversProvider;

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
        this.actions = new BlindActionsProvider().get();
    }

    public void executeFor(String blindName, String actionName) {
        BlindDriver blindDriver = blinds.computeIfAbsent(blindName, THROW_NO_BLIND_FOUND);
        actions.getOrDefault(actionName, THROW_ILLEGAL_ACTION_EXCEPTION).accept(blindDriver);
    }

    public void stopAll() {
        blinds.values().forEach(BlindDriver::stop);
    }
}
