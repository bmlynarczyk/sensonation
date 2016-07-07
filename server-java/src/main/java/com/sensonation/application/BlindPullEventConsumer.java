package com.sensonation.application;

import com.sensonation.domain.BlindActionsExecutor;
import com.sensonation.domain.BlindEvent;
import lombok.SneakyThrows;
import org.springframework.core.task.TaskExecutor;

import java.util.concurrent.BlockingQueue;

public class BlindPullEventConsumer {

    private final TaskExecutor taskExecutor;
    private final BlockingQueue<BlindEvent> blindEvents;
    private final BlindActionsExecutor blindActionsExecutor;

    public BlindPullEventConsumer(TaskExecutor taskExecutor, BlockingQueue<BlindEvent> blindEvents, BlindActionsExecutor blindActionsExecutor) {
        this.taskExecutor = taskExecutor;
        this.blindEvents = blindEvents;
        this.blindActionsExecutor = blindActionsExecutor;
        taskExecutor.execute(this::run);
    }

    @SneakyThrows
    private void run() {
        while (true){
            BlindEvent blindEvent = blindEvents.take();
            blindActionsExecutor.executeFor(blindEvent.getBlindName(), blindEvent.getActionName());
        }
    }
}
