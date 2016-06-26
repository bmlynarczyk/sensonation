package com.sensonation.application;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class ScheduledTaskStoreProvider implements Supplier<Map<ScheduledTaskName, ScheduledTask>> {

    private final Map<ScheduledTaskName, ScheduledTask> map = new ConcurrentHashMap<>();

    @Override
    public Map<ScheduledTaskName, ScheduledTask> get() {
        return map;
    }
}
