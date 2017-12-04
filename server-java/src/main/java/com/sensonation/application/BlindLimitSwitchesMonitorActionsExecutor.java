package com.sensonation.application;

import com.google.common.collect.ImmutableMap;
import com.sensonation.domain.BlindEvent;

import java.util.Map;
import java.util.function.Consumer;

public class BlindLimitSwitchesMonitorActionsExecutor {

    private final Map<String, Consumer<BlindEvent>> monitorActionExecutor;

    public BlindLimitSwitchesMonitorActionsExecutor(BlindLimitSwitchesMonitor blindLimitSwitchesMonitor) {
        this.monitorActionExecutor = ImmutableMap.of(
                "switchMonitor", blindEvent -> blindLimitSwitchesMonitor.switchMonitor(),
                "pullUpUnfinishedBlinds", blindEvent -> blindLimitSwitchesMonitor.pullUpUnfinishedBlinds(),
                "pullDownUnfinishedBlinds", blindEvent -> blindLimitSwitchesMonitor.pullDownUnfinishedBlinds(),
                "pullUpUnfinishedBlind", blindEvent -> blindLimitSwitchesMonitor.pullUpUnfinishedBlind(blindEvent.getBlindName()),
                "pullDownUnfinishedBlind", blindEvent -> blindLimitSwitchesMonitor.pullDownUnfinishedBlind(blindEvent.getBlindName())
        );
    }

    boolean shouldExecute(BlindEvent blindEvent){
        return monitorActionExecutor.keySet().contains(blindEvent.getActionName());
    }

    void execute(BlindEvent blindEvent){
        monitorActionExecutor.get(blindEvent.getActionName()).accept(blindEvent);
    }

}
