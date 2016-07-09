package com.sensonation.application;

import org.junit.Test;

import java.time.Clock;
import java.time.Instant;

import static com.sensonation.InstantTestUtils.getDate;
import static java.time.ZoneId.systemDefault;
import static org.assertj.core.api.Assertions.assertThat;

public class SunServiceTest {

    @Test
    public void test_winter_sunrise_date_time(){
        Instant date = getDate(2015, 12, 27, 0, 0);
        SunService service = new SunService(Clock.fixed(date, systemDefault()));
        Instant sunriseDate = service.getSunriseDate(date);
        assertThat(sunriseDate).isEqualTo(getDate(2015, 12, 27, 7, 39, 24, 452000000));
    }


    @Test
    public void test_correct_summer_sunrise_date_time(){
        Instant date = getDate(2015, 8, 23, 0, 0);
        SunService service = new SunService(Clock.fixed(date, systemDefault()));
                Instant sunriseDate = service.getSunriseDate(date);
        assertThat(sunriseDate).isEqualTo(getDate(2015, 8, 23, 5, 32, 52, 234000000));
    }


    @Test
    public void test_winter_sunset_date_time(){
        Instant date = getDate(2015, 12, 27, 0, 0);
        SunService service = new SunService(Clock.fixed(date, systemDefault()));
                Instant sunriseDate = service.getSunsetDate(date);
        assertThat(sunriseDate).isEqualTo(getDate(2015, 12, 27, 15, 28, 28, 185000000));
    }


    @Test
    public void test_summer_sunset_date_time(){
        Instant date = getDate(2015, 8, 23, 0, 0);
        SunService service = new SunService(Clock.fixed(date, systemDefault()));
                Instant sunriseDate = service.getSunsetDate(date);
        assertThat(sunriseDate).isEqualTo(getDate(2015, 8, 23, 19, 38, 43, 330000000));
    }

}