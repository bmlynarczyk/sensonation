package com.sensonation.application;

import com.sensonation.config.prod.BlindsDriversConfig;
import com.sensonation.domain.BlindDriver;

import java.util.Map;

public class BlindDriversProvider {

    private final BlindsDriversConfig config;

    public BlindDriversProvider(BlindsDriversConfig config) {
        this.config = config;
    }

    public Map<String, BlindDriver> get() {
        return config.get();
    }

}