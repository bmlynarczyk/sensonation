package com.sensonation.application;

import com.sensonation.domain.BlindSchedulerPolicy;
import com.sensonation.domain.DefaultBlindSchedulerPolicy;
import com.sensonation.domain.ScheduledTask;
import com.sensonation.domain.ScheduledTaskName;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.scheduling.TaskScheduler;

import java.time.*;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static com.sensonation.InstantTestUtils.getDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class BlindSchedulerTest {

    private final BlindService blindActionsExecutor = mock(BlindService.class);
    private final Clock clock = mock(Clock.class);
    private final SunService sunService = new SunService(clock);
    private final BlindSchedulerPolicy schedulerPolicy = new DefaultBlindSchedulerPolicy(sunService, clock);
    private final TaskScheduler taskScheduler = mock(TaskScheduler.class);


    @Before
    public void setUp(){
        reset(blindActionsExecutor, clock, taskScheduler);
    }

    @Test
    public void test_initialisation_when_now_is_before_sunrise_and_after_1_am(){
        when(clock.instant()).thenReturn(getDate(2016, 8, 23, 1, 0));
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        BlindScheduler blindScheduler = new BlindScheduler(schedulerPolicy, taskScheduler, blindActionsExecutor, sunService);
        Map<ScheduledTaskName, ScheduledTask> scheduledTasks = blindScheduler.getScheduledTasks();

        verify(taskScheduler, times(3)).schedule(Matchers.any(Runnable.class), (Date) Matchers.anyObject());
        assertThat(scheduledTasks.size()).isEqualTo(3);
        assertThat(scheduledTasks.get(ScheduledTaskName.PULL_UP).getExecutionDate()).isEqualTo(getDate(2016, 8, 23, 6, 45));
        assertThat(scheduledTasks.get(ScheduledTaskName.PULL_DOWN).getExecutionDate()).isEqualTo(getDate(2016, 8, 23, 19, 37, 10, 420000000));
        assertThat(scheduledTasks.get(ScheduledTaskName.RECALC).getExecutionDate()).isEqualTo(sunService.tomorrowAt1Am());
    }

    @Test
    public void test_initialisation_when_now_is_after_sunrise(){
        when(clock.instant()).thenReturn(getDate(2016, 8, 23, 8, 0));
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        BlindScheduler blindScheduler = new BlindScheduler(schedulerPolicy, taskScheduler, blindActionsExecutor, sunService);
        Map<ScheduledTaskName, ScheduledTask> scheduledTasks = blindScheduler.getScheduledTasks();

        verify(taskScheduler, times(2)).schedule(Matchers.any(Runnable.class), (Date) Matchers.anyObject());
        assertThat(scheduledTasks.size()).isEqualTo(2);
        assertThat(scheduledTasks.get(ScheduledTaskName.PULL_DOWN).getExecutionDate()).isEqualTo(getDate(2016, 8, 23, 19, 37, 10, 420000000));
        assertThat(scheduledTasks.get(ScheduledTaskName.RECALC).getExecutionDate()).isEqualTo(sunService.tomorrowAt1Am());
    }

    private Instant tomorrowAt1Am() {
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(clock), LocalTime.MIDNIGHT);
        dateTime = dateTime.plusDays(1);
        dateTime = dateTime.plusHours(1);
        return dateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

}