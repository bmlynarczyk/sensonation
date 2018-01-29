package com.sensonation.application;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.sensonation.domain.BlindEvent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;

import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class BlindEventBus {

    private final BlockingQueue<BlindEvent> blindEvents;
    private final List<ActionsExecutor> actionsExecutors;
    private Integer timeoutInSeconds;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public BlindEventBus(TaskExecutor taskExecutor, BlockingQueue<BlindEvent> blindEvents,
                         List<ActionsExecutor> actionsExecutors, Integer timeoutInSeconds) {
        this.blindEvents = blindEvents;
        this.actionsExecutors = actionsExecutors;
        this.timeoutInSeconds = timeoutInSeconds;
        taskExecutor.execute(this::run);
    }

    @SneakyThrows
    private void run() {
        while (true) {
            BlindEvent blindEvent = blindEvents.take();
            Stopwatch stopwatch = new Stopwatch();
            stopwatch.start();
            try{
                executorService.submit(() -> forward(blindEvent)).get(timeoutInSeconds, TimeUnit.SECONDS);
                log.info("event {} executed in {}", blindEvent.getActionName(), stopwatch.toString());
            } catch (TimeoutException e){
                log.info("timeout of {} second occurs for event {} execution", blindEvent.getActionName(), timeoutInSeconds);
                blindEvents.drainTo(Lists.newArrayList());
                blindEvents.put(BlindEvent.builder().actionName("blindsTimeoutStop").build());
            }
        }
    }

    private void forward(BlindEvent blindEvent) {
        actionsExecutors.stream()
                .filter(actionsExecutor -> actionsExecutor.shouldExecute(blindEvent))
                .forEach(actionsExecutor -> actionsExecutor.execute(blindEvent));
    }


}
