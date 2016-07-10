package com.sensonation.application;

import com.sensonation.domain.BlindSchedulerPolicy;
import com.sensonation.domain.ScheduledTask;
import com.sensonation.domain.ScheduledTaskName;
import org.springframework.scheduling.TaskScheduler;

import java.time.*;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
                          BlindService blindActionsExecutor,
                          SunService sunService) {
        this.policy = policy;
        this.taskScheduler = taskScheduler;
        pullUpRunner = blindActionsExecutor::pullUpAllBlinds;
        pullDownRunner = blindActionsExecutor::pullDownAllBlinds;
        this.sunService = sunService;
        this.scheduledTaskStore = new ConcurrentHashMap<>();
        init();
    }

    public Map<ScheduledTaskName, ScheduledTask> getScheduledTasks() {
        return scheduledTaskStore;
    }

    private void init(){
        if(sunService.isAfterTodayAt1Am() &&  sunService.isBeforeSunrise())
            addPullUpTask();
        if(sunService.isAfterTodayAt1Am() &&  sunService.isBeforeSunset())
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
            ScheduledTask unscheduledTask = scheduledTaskStore.put(ScheduledTaskName.PULL_DOWN, new ScheduledTask(instant, schedule));
            if(unscheduledTask != null)
                handleUnscheduledTask(unscheduledTask);
        });
    }

    private void addPullUpTask() {
        policy.getPullUpDateTime().ifPresent(instant -> {
            ScheduledFuture<?> schedule = taskScheduler.schedule(pullUpRunner, asDate(instant));
            ScheduledTask unscheduledTask = scheduledTaskStore.put(ScheduledTaskName.PULL_UP, new ScheduledTask(instant, schedule));
            if(unscheduledTask != null)
                handleUnscheduledTask(unscheduledTask);
        });
    }

    private void addRecalcTask() {
        Instant tomorrowAt1Am = sunService.tomorrowAt1Am();
        ScheduledFuture<?> schedule = taskScheduler.schedule(recalcRunner, asDate(tomorrowAt1Am));
        scheduledTaskStore.put(ScheduledTaskName.RECALC, new ScheduledTask(tomorrowAt1Am, schedule));
    }

    private void handleUnscheduledTask(ScheduledTask unscheduledTask) {
        ScheduledFuture<?> unscheduledFuture = unscheduledTask.getScheduledFuture();
        if(!unscheduledFuture.isDone())
            unscheduledFuture.cancel(false);
    }

    private Date asDate(Instant instant) {
        return Date.from(instant);
    }
}
