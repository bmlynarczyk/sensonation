package com.sensonation.domain;

import com.sensonation.application.SunService;
import org.junit.Test;

import java.time.Clock;

import static com.sensonation.InstantTestUtils.getDate;
import static java.time.ZoneId.systemDefault;
import static org.assertj.core.api.Assertions.assertThat;

public class DefaultBlindSchedulerPolicyTest {

    @Test
    public void should_return_pull_up_time_at_6_45_am_when_sunrise_is_earlier(){
        Clock clock = Clock.fixed(getDate(2015, 8, 23, 0, 0), systemDefault());
        SunService sunService = new SunService(clock);
        BlindSchedulerPolicy policy = new DefaultBlindSchedulerPolicy(sunService, clock);
        assertThat(policy.getPullUpDateTime()).isPresent();
        assertThat(policy.getPullUpDateTime().get())
                .isEqualTo(getDate(2015, 8, 23, 6, 45));
    }

    @Test
    public void should_return_pull_up_time_at_sunrise_when_sunrise_is_after_6_45_am(){
        Clock clock = Clock.fixed(getDate(2015, 12, 27, 0, 0), systemDefault());
        SunService sunService = new SunService(clock);
        BlindSchedulerPolicy policy = new DefaultBlindSchedulerPolicy(sunService, clock);
        assertThat(policy.getPullUpDateTime()).isPresent();
        assertThat(policy.getPullUpDateTime().get())
                .isEqualTo(getDate(2015, 12, 27, 7, 39, 24, 452000000));
    }

}