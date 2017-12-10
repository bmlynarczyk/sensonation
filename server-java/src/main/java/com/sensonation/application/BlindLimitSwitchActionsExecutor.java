package com.sensonation.application;

import com.google.common.collect.ImmutableMap;
import com.sensonation.domain.BlindEvent;

import java.util.Map;
import java.util.function.Consumer;

public class BlindLimitSwitchActionsExecutor implements ActionsExecutor {

    private final Map<String, Consumer<BlindEvent>> monitorActionExecutor;

    public BlindLimitSwitchActionsExecutor(BlindUnfinishedPullingService blindUnfinishedPullingService) {
        this.monitorActionExecutor = ImmutableMap.of(
                "pullUpUnfinishedBlind", blindEvent -> blindUnfinishedPullingService.pullUpUnfinishedBlind(blindEvent.getBlindName()),
                "pullDownUnfinishedBlind", blindEvent -> blindUnfinishedPullingService.pullDownUnfinishedBlind(blindEvent.getBlindName())
        );
    }

    public boolean shouldExecute(BlindEvent blindEvent){
        return monitorActionExecutor.keySet().contains(blindEvent.getActionName());
    }

    public void execute(BlindEvent blindEvent){
        monitorActionExecutor.get(blindEvent.getActionName()).accept(blindEvent);
    }

}