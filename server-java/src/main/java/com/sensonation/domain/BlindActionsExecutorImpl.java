package com.sensonation.domain;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
public class BlindActionsExecutorImpl implements BlindActionsExecutor {

    private static final Consumer<BlindDriver> THROW_ILLEGAL_ACTION_EXCEPTION = blind -> {
        throw new IllegalArgumentException("unknown action");
    };

    private static final Function<String, BlindDriver> THROW_NO_BLIND_FOUND = key -> {
        throw new NoSuchElementException("unknown blind");
    };

    private final Map<String, BlindDriver> blinds;

    private final Map<String, Consumer<BlindDriver>> actions;

    public BlindActionsExecutorImpl(Supplier<Map<String, BlindDriver>> blindsProvider) {
        this.blinds = blindsProvider.get();
        this.actions = new BlindActionsProvider().get();
    }

    @Override
    public void executeFor(String blindName, String actionName) {
        BlindDriver blindDriver = blinds.computeIfAbsent(blindName, THROW_NO_BLIND_FOUND);
        actions.getOrDefault(actionName, THROW_ILLEGAL_ACTION_EXCEPTION).accept(blindDriver);
    }

    @Override
    public void stopAll() {
        blinds.values().stream().forEach(BlindDriver::stop);
    }

}
