package com.sensonation.application;

import com.sensonation.domain.BlindEvent;
import lombok.SneakyThrows;
import org.springframework.core.task.TaskExecutor;

import java.util.concurrent.BlockingQueue;

public class BlindEventBus {

    private final BlockingQueue<BlindEvent> blindEvents;
    private final BlindActionsExecutor blindActionsExecutor;
    private final BlindLimitSwitchesMonitorActionsExecutor blindLimitSwitchesMonitorActionsExecutor;

    public BlindEventBus(TaskExecutor taskExecutor, BlockingQueue<BlindEvent> blindEvents,
                         BlindActionsExecutor blindActionsExecutor,
                         BlindLimitSwitchesMonitorActionsExecutor blindLimitSwitchesMonitorActionsExecutor) {
        this.blindEvents = blindEvents;
        this.blindActionsExecutor = blindActionsExecutor;
        this.blindLimitSwitchesMonitorActionsExecutor = blindLimitSwitchesMonitorActionsExecutor;
        taskExecutor.execute(this::run);
    }

    @SneakyThrows
    private void run() {
        while (true) {
            BlindEvent blindEvent = blindEvents.take();
            forward(blindEvent);
        }
    }

    public void forward(BlindEvent blindEvent) {
        if (blindActionsExecutor.shouldExecute(blindEvent))
            blindActionsExecutor.execute(blindEvent);
        if (blindLimitSwitchesMonitorActionsExecutor.shouldExecute(blindEvent))
            blindLimitSwitchesMonitorActionsExecutor.execute(blindEvent);
    }


}
