package com.sensonation.domain;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BlindActionsProvider implements Supplier<Map<String, Consumer<BlindDriver>>> {

    private static final ImmutableMap<String, Consumer<BlindDriver>> actions = ImmutableMap.of(
            "pullDown", BlindDriver::pullDown,
            "pullUp", BlindDriver::pullUp,
            "stop", BlindDriver::stop
    );

    @Override
    public Map<String, Consumer<BlindDriver>> get() {
        return actions;
    }
}
