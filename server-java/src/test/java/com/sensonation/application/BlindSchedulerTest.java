package com.sensonation.application;

import com.google.common.collect.Maps;
import com.sensonation.domain.BlindSchedulerPolicy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.scheduling.TaskScheduler;

import java.time.*;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static java.time.ZoneId.systemDefault;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class BlindSchedulerTest {

    private final BlindService blindActionsExecutor = mock(BlindService.class);
    private final BlindSchedulerPolicy policy = mock(BlindSchedulerPolicy.class);
    private final SunService sunService = mock(SunService.class);
    private final TaskScheduler taskScheduler = mock(TaskScheduler.class);
    private Map<ScheduledTaskName, ScheduledTask> scheduledTaskStore;

    @Before
    public void setUp(){
        Mockito.reset(blindActionsExecutor, policy, sunService, taskScheduler);
        scheduledTaskStore = Maps.newHashMap();
    }

    @Test
    public void test_initialisation_when_now_is_before_sunrise(){
        Instant pullDownDate = getDate(2015, 12, 27, 15, 28, 28, 185000000);
        Instant pullUpDate = getDate(2015, 12, 27, 7, 28, 28, 185000000);

        when(sunService.isBeforeSunrise()).thenReturn(true);
        when(sunService.isBeforeSunset()).thenReturn(true);
        when(policy.getPullUpDateTime()).thenReturn(Optional.of(pullUpDate));
        when(policy.getPullDownDateTime()).thenReturn(Optional.of(pullDownDate));
        new BlindScheduler(policy, taskScheduler, blindActionsExecutor, sunService, scheduledTaskStore);
        verify(taskScheduler, times(3)).schedule(Matchers.any(Runnable.class), (Date) Matchers.anyObject());
        assertThat(scheduledTaskStore.size()).isEqualTo(3);
        assertThat(scheduledTaskStore.get(ScheduledTaskName.PULL_UP).getExecutionDate()).isEqualTo(pullUpDate);
        assertThat(scheduledTaskStore.get(ScheduledTaskName.PULL_DOWN).getExecutionDate()).isEqualTo(pullDownDate);
        assertThat(scheduledTaskStore.get(ScheduledTaskName.RECALC).getExecutionDate()).isEqualTo(tomorrowAt1Am());
    }

    @Test
    public void test_initialisation_when_now_is_after_sunrise(){
        when(sunService.isBeforeSunrise()).thenReturn(false);
        when(sunService.isBeforeSunset()).thenReturn(true);
        Instant pullDownDate = getDate(2015, 12, 27, 15, 28, 28, 185000000);
        Optional<Instant> date = Optional.of(pullDownDate);
        when(policy.getPullDownDateTime()).thenReturn(date);
        new BlindScheduler(policy, taskScheduler, blindActionsExecutor, sunService, scheduledTaskStore);
        verify(taskScheduler, times(2)).schedule(Matchers.any(Runnable.class), (Date) Matchers.anyObject());
        assertThat(scheduledTaskStore.size()).isEqualTo(2);
        assertThat(scheduledTaskStore.get(ScheduledTaskName.PULL_DOWN).getExecutionDate()).isEqualTo(pullDownDate);
        assertThat(scheduledTaskStore.get(ScheduledTaskName.RECALC).getExecutionDate()).isEqualTo(tomorrowAt1Am());
    }

    @Test
    public void test_initialisation_when_now_is_after_sunset(){
        when(sunService.isBeforeSunrise()).thenReturn(false);
        when(sunService.isBeforeSunset()).thenReturn(false);
        new BlindScheduler(policy, taskScheduler, blindActionsExecutor, sunService, scheduledTaskStore);
        verify(taskScheduler, times(1)).schedule(Matchers.any(Runnable.class), (Date) Matchers.anyObject());
        assertThat(scheduledTaskStore.size()).isEqualTo(1);
        assertThat(scheduledTaskStore.get(ScheduledTaskName.RECALC).getExecutionDate()).isEqualTo(tomorrowAt1Am());
    }

    private Instant getDate(int year, int month, int dayOfMonth, int hour, int minute, int second, int nanoOfSecond) {
        return LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond).atZone(systemDefault()).toInstant();
    }

    private Instant tomorrowAt1Am() {
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(ZoneId.systemDefault()), LocalTime.MIDNIGHT);
        dateTime = dateTime.plusDays(1);
        dateTime = dateTime.plusHours(1);
        return dateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

}