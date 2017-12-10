package com.sensonation.application;

import com.sensonation.config.BlindsDriversConfig;
import com.sensonation.domain.BlindDriver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlindDriversProvider {

    private final BlindsDriversConfig config;

    public BlindDriversProvider(BlindsDriversConfig config) {
        this.config = config;
    }

    public Map<String, BlindDriver> get() {
        return new ConcurrentHashMap<>(config.get());
    }

}
