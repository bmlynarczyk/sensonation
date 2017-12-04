package com.sensonation.application;

import org.springframework.core.task.TaskExecutor;

public class BlindStopper {

    private final TaskExecutor taskExecutor;
    private final BlindActionsExecutor blindActionsExecutor;

    public BlindStopper(TaskExecutor taskExecutor, BlindActionsExecutor blindActionsExecutor) {
        this.taskExecutor = taskExecutor;
        this.blindActionsExecutor = blindActionsExecutor;
    }

    void stop() {
        taskExecutor.execute(blindActionsExecutor::stopAll);
    }

}
