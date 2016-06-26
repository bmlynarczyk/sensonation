package com.sensonation.domain;

import com.sensonation.application.SunService;
import com.sensonation.application.SunServiceImpl;
import com.sensonation.domain.BlindSchedulerPolicy;
import com.sensonation.domain.DefaultBlindSchedulerPolicy;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;

import static java.time.ZoneId.systemDefault;
import static org.assertj.core.api.Assertions.assertThat;

public class DefaultBlindSchedulerPolicyTest {

    @Test
    public void should_return_pull_up_time_at_6_45_am_when_sunrise_is_earlier(){
        Clock clock = Clock.fixed(getDate(2015, 8, 23, 0, 0), systemDefault());
        SunService sunService = new SunServiceImpl(clock);
        BlindSchedulerPolicy policy = new DefaultBlindSchedulerPolicy(sunService, clock);
        assertThat(policy.getPullUpDateTime()).isPresent();
        assertThat(policy.getPullUpDateTime().get())
                .isEqualTo(getDate(2015, 8, 23, 6, 45));
    }

    @Test
    public void should_return_pull_up_time_at_sunrise_when_sunrise_is_after_6_45_am(){
        Clock clock = Clock.fixed(getDate(2015, 12, 27, 0, 0), systemDefault());
        SunService sunService = new SunServiceImpl(clock);
        BlindSchedulerPolicy policy = new DefaultBlindSchedulerPolicy(sunService, clock);
        assertThat(policy.getPullUpDateTime()).isPresent();
        assertThat(policy.getPullUpDateTime().get())
                .isEqualTo(getDate(2015, 12, 27, 7, 39, 24, 452000000));
    }

    private Instant getDate(int year, int month, int dayOfMonth, int hour, int minute) {
        return LocalDateTime.of(year, month, dayOfMonth, hour, minute).atZone(systemDefault()).toInstant();
    }

    private Instant getDate(int year, int month, int dayOfMonth, int hour, int minute, int second, int nanoOfSecond) {
        return LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond).atZone(systemDefault()).toInstant();
    }

}