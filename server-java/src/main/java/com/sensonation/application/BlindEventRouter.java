package com.sensonation.application;

import com.google.common.collect.ImmutableSet;
import com.sensonation.domain.BlindActionsExecutor;
import com.sensonation.domain.BlindEvent;
import lombok.SneakyThrows;
import org.springframework.core.task.TaskExecutor;

import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class BlindEventRouter {

    private final BlockingQueue<BlindEvent> blindEvents;
    private final BlindActionsExecutor blindActionsExecutor;
    private final Set<String> driverActions = ImmutableSet.of("pullUp", "pullDown", "stop");
    private final Set<String> monitorActions = ImmutableSet.of("switchMonitor");
    private final BlindLimitSwitchesMonitor blindLimitSwitchesMonitor;

    public BlindEventRouter(TaskExecutor taskExecutor, BlockingQueue<BlindEvent> blindEvents, BlindActionsExecutor blindActionsExecutor, BlindLimitSwitchesMonitor blindLimitSwitchesMonitor) {
        this.blindEvents = blindEvents;
        this.blindActionsExecutor = blindActionsExecutor;
        this.blindLimitSwitchesMonitor = blindLimitSwitchesMonitor;
        taskExecutor.execute(this::run);
    }

    @SneakyThrows
    private void run() {
        while (true){
            BlindEvent blindEvent = blindEvents.take();
            forward(blindEvent);
        }
    }

    public void forward(BlindEvent blindEvent){
        if(driverActions.contains(blindEvent.getActionName()))
            blindActionsExecutor.executeFor(blindEvent.getBlindName(), blindEvent.getActionName());
        if(monitorActions.contains(blindEvent.getActionName()))
            blindLimitSwitchesMonitor.switchMonitor();
    }




}
