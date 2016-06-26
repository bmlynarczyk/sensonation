package com.sensonation.application;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.time.ZoneId.systemDefault;
import static org.assertj.core.api.Assertions.assertThat;

public class SunServiceImplTest {

    @Test
    public void test_winter_sunrise_date_time(){
        Instant date = getDate(2015, 12, 27, 0, 0);
        SunService service = new SunServiceImpl(Clock.fixed(date, systemDefault()));
        Instant sunriseDate = service.getSunriseDate(date);
        assertThat(sunriseDate).isEqualTo(getDate(2015, 12, 27, 7, 39, 24, 452000000));
    }


    @Test
    public void test_correct_summer_sunrise_date_time(){
        Instant date = getDate(2015, 8, 23, 0, 0);
        SunService service = new SunServiceImpl(Clock.fixed(date, systemDefault()));
                Instant sunriseDate = service.getSunriseDate(date);
        assertThat(sunriseDate).isEqualTo(getDate(2015, 8, 23, 5, 32, 52, 234000000));
    }


    @Test
    public void test_winter_sunset_date_time(){
        Instant date = getDate(2015, 12, 27, 0, 0);
        SunService service = new SunServiceImpl(Clock.fixed(date, systemDefault()));
                Instant sunriseDate = service.getSunsetDate(date);
        assertThat(sunriseDate).isEqualTo(getDate(2015, 12, 27, 15, 28, 28, 185000000));
    }


    @Test
    public void test_summer_sunset_date_time(){
        Instant date = getDate(2015, 8, 23, 0, 0);
        SunService service = new SunServiceImpl(Clock.fixed(date, systemDefault()));
                Instant sunriseDate = service.getSunsetDate(date);
        assertThat(sunriseDate).isEqualTo(getDate(2015, 8, 23, 19, 38, 43, 330000000));
    }

    private Instant getDate(int year, int month, int dayOfMonth, int hour, int minute) {
        return LocalDateTime.of(year, month, dayOfMonth, hour, minute).atZone(systemDefault()).toInstant();
    }

    private Instant getDate(int year, int month, int dayOfMonth, int hour, int minute, int second, int nanoOfSecond) {
        return LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond).atZone(systemDefault()).toInstant();
    }

}