package com.sensonation.application;

import com.sensonation.domain.BlindSchedulerPolicy;
import com.sensonation.domain.ScheduledTask;
import com.sensonation.domain.ScheduledTaskName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
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
            ScheduledTask scheduledTask = new ScheduledTask(instant, schedule);
            log.info("pull down task added, it will be executed {}", asFormattedDate(scheduledTask.getExecutionDate()));
            ScheduledTask unscheduledTask = scheduledTaskStore.put(ScheduledTaskName.PULL_DOWN, scheduledTask);
            if(unscheduledTask != null)
                handleUnscheduledTask(unscheduledTask);
        });
    }

    private void addPullUpTask() {
        policy.getPullUpDateTime().ifPresent(instant -> {
            ScheduledFuture<?> schedule = taskScheduler.schedule(pullUpRunner, asDate(instant));
            ScheduledTask scheduledTask = new ScheduledTask(instant, schedule);
            log.info("pull up task added, it will be executed {}", asFormattedDate(scheduledTask.getExecutionDate()));
            ScheduledTask unscheduledTask = scheduledTaskStore.put(ScheduledTaskName.PULL_UP, scheduledTask);
            if(unscheduledTask != null)
                handleUnscheduledTask(unscheduledTask);
        });
    }

    private void addRecalcTask() {
        Instant tomorrowAt1Am = sunService.tomorrowAt1Am();
        ScheduledFuture<?> schedule = taskScheduler.schedule(recalcRunner, asDate(tomorrowAt1Am));
        ScheduledTask scheduledTask = new ScheduledTask(tomorrowAt1Am, schedule);
        log.info("recalac task added, it will be executed {}", asFormattedDate(scheduledTask.getExecutionDate()));
        scheduledTaskStore.put(ScheduledTaskName.RECALC, scheduledTask);
    }

    private void handleUnscheduledTask(ScheduledTask unscheduledTask) {
        ScheduledFuture<?> unscheduledFuture = unscheduledTask.getScheduledFuture();
        if(!unscheduledFuture.isDone())
            unscheduledFuture.cancel(false);
    }

    private Date asDate(Instant instant) {
        return Date.from(instant);
    }

    private String asFormattedDate(Instant instant){
        return DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                .withLocale( Locale.getDefault() )
                .withZone( ZoneId.systemDefault())
                .format(instant);
    }
}
