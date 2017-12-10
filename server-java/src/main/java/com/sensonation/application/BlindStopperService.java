package com.sensonation.application;

import com.sensonation.domain.BlindDriver;
import org.springframework.core.task.TaskExecutor;

import java.util.Map;

public class BlindStopperService {

    private final Map<String, BlindDriver> blinds;

    public BlindStopperService(BlindDriversProvider blindsProvider) {
        blinds = blindsProvider.get();
    }

    void stop() {
        blinds.values().forEach(BlindDriver::stop);
    }

}
