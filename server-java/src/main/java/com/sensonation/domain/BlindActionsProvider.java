package com.sensonation.domain;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BlindActionsProvider implements Supplier<Map<String, Consumer<Blind>>> {

    private static final ImmutableMap<String, Consumer<Blind>> actions = ImmutableMap.of(
            "activate", Blind::activate,
            "deactivate", Blind::deactivate,
            "stop", Blind::stop,
            "pullDown", Blind::pullDown,
            "pullUp", Blind::pullUp
    );

    @Override
    public Map<String, Consumer<Blind>> get() {
        return actions;
    }
}
