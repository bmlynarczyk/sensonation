package com.sensonation.config;

import com.sensonation.application.*;
import com.sensonation.domain.BlindEvent;
import org.springframework.cloud.context.restart.RestartEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

@Configuration
public class BlindEventConfig {

    @Bean
    public ArrayBlockingQueue<BlindEvent> blindEvents() {
        return new ArrayBlockingQueue<>(20);
    }

    @Bean
    public ActionsExecutor blindActionsExecutor(BlindDriversProvider blindDriversProvider) {
        return new BlindActionsExecutor(blindDriversProvider);
    }


    @Bean
    public ActionsExecutor blindLimitSwitchesActionExecutor(BlindUnfinishedPullingService blindUnfinishedPullingService) {
        return new BlindLimitSwitchActionsExecutor(blindUnfinishedPullingService);
    }

    @Bean
    public BlindEventBus blindEventBus(TaskExecutor blindTaskExecutor,
                                       ArrayBlockingQueue<BlindEvent> blindEvents,
                                       List<ActionsExecutor> actionsExecutors,
                                       RestartEndpoint restartEndpoint) {
        return new BlindEventBus(blindTaskExecutor, blindEvents, actionsExecutors, 299, restartEndpoint);
    }

}