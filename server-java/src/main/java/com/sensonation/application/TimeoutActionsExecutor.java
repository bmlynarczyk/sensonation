package com.sensonation.application;

import com.google.common.collect.ImmutableMap;
import com.sensonation.domain.BlindEvent;

import java.util.Map;

public class TimeoutActionsExecutor implements ActionsExecutor {

    private final BlindStopperService blindStopperService;

    private final Map<String, Runnable> actions;

    public TimeoutActionsExecutor(BlindStopperService blindStopperService) {
        this.blindStopperService = blindStopperService;
        this.actions = ImmutableMap.<String, Runnable>builder()
                .put("blindsTimeoutStop", this::blindsTimeoutStop)
                .build();
    }

    private void blindsTimeoutStop() {
        blindStopperService.stop();
    }

    @Override
    public boolean shouldExecute(BlindEvent blindEvent) {
        return actions.keySet().contains(blindEvent.getActionName());
    }

    @Override
    public void execute(BlindEvent blindEvent) {
        actions.get(blindEvent.getActionName()).run();
    }
}
