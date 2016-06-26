package com.sensonation.application;

import com.sensonation.domain.BlindActionsExecutor;
import com.sensonation.domain.BlindSchedulerPolicy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;

import java.time.*;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

public class BlindScheduler {

    private final TaskScheduler taskScheduler;
    private final Runnable pullUpRunner;
    private final Runnable pullDownRunner;
    private final Runnable recalcRunner = this::recalc;
    private final SunService sunService;
    private final BlindSchedulerPolicy policy;
    private final Map<ScheduledTaskName, ScheduledTask> scheduledTaskStore;

    public BlindScheduler(BlindSchedulerPolicy policy,
                          TaskScheduler taskScheduler,
                          BlindActionsExecutor blindActionsExecutor,
                          SunService sunService,
                          Map<ScheduledTaskName, ScheduledTask> scheduledTaskStore) {
        this.policy = policy;
        this.taskScheduler = taskScheduler;
        pullUpRunner = blindActionsExecutor::pullUpAllBlinds;
        pullDownRunner = blindActionsExecutor::pullDownAllBlinds;
        this.sunService = sunService;
        this.scheduledTaskStore = scheduledTaskStore;
        init();
    }

    private void init(){
        if(sunService.isBeforeSunrise())
            addPullUpTask();
        if(sunService.isBeforeSunset())
            addPullDownTask();
        addRecalcTask();
    }

    private void recalc(){
        addPullUpTask();
        addPullDownTask();
        addRecalcTask();
    }

    private void addPullDownTask() {
        policy.getPullDownDateTime().ifPresent(instant -> {
            ScheduledFuture<?> schedule = taskScheduler.schedule(pullDownRunner, asDate(instant));
            scheduledTaskStore.put(ScheduledTaskName.PULL_DOWN, new ScheduledTask(instant, schedule));
        });
    }

    private void addPullUpTask() {
        policy.getPullUpDateTime().ifPresent(instant -> {
            ScheduledFuture<?> schedule = taskScheduler.schedule(pullUpRunner, asDate(instant));
            scheduledTaskStore.put(ScheduledTaskName.PULL_UP, new ScheduledTask(instant, schedule));
        });
    }

    private void addRecalcTask() {
        Instant tomorrowAt1Am = tomorrowAt1Am();
        ScheduledFuture<?> schedule = taskScheduler.schedule(recalcRunner, asDate(tomorrowAt1Am));
        scheduledTaskStore.put(ScheduledTaskName.RECALC, new ScheduledTask(tomorrowAt1Am, schedule));
    }

    private Instant tomorrowAt1Am() {
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(ZoneId.systemDefault()), LocalTime.MIDNIGHT);
        dateTime = dateTime.plusDays(1);
        dateTime = dateTime.plusHours(1);
        return dateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    private Date asDate(Instant instant) {
        return Date.from(instant);
    }

}