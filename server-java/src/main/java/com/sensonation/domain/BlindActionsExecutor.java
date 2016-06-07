package com.sensonation.domain;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
public class BlindActionsExecutor implements BiConsumer<String, String>{

    private static final Consumer<Blind> THROW_ILLEGAL_ACTION_EXCEPTION = blind -> {
        throw new IllegalArgumentException("unknown action");
    };

    private static final Function<String, Blind> THROW_NO_BLIND_FOUND = key -> {
        throw new NoSuchElementException("unknown blind");
    };

    private final Map<String, Blind> blinds;

    private final Map<String, Consumer<Blind>> actions;

    public BlindActionsExecutor(Supplier<Map<String, Blind>> blindsProvider, Supplier<Map<String, Consumer<Blind>>> actionsProvider) {
        this.blinds = blindsProvider.get();
        this.actions = actionsProvider.get();
    }

    @Override
    public void accept(String blindName, String actionName) {
        Blind blind = blinds.computeIfAbsent(blindName, THROW_NO_BLIND_FOUND);
        if (blind.isActive()) {
            log.info("call {}", actionName);
            actions.getOrDefault(actionName, THROW_ILLEGAL_ACTION_EXCEPTION).accept(blind);
        } else if ("activate".equals(actionName)) {
            blind.activate();
        } else {
            log.info("blind {} is inactive", blind.isActive());
        }
    }


}
