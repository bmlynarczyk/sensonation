package com.sensonation.domain;

import com.sensonation.application.SunService;

import java.time.*;
import java.util.Optional;

import static java.time.Instant.now;

public class DefaultBlindSchedulerPolicy implements BlindSchedulerPolicy {

    private final SunService sunService;
    private final Clock clock;

    public DefaultBlindSchedulerPolicy(SunService sunService, Clock clock) {
        this.sunService = sunService;
        this.clock = clock;
    }

    @Override
    public Optional<Instant> getPullUpDateTime() {
        Instant sunriseDate = sunService.getSunriseDate(now(clock));
        Instant todayAt645Am = todayAt645Am();
        if(sunriseDate.isAfter(todayAt645Am))
            return Optional.of(sunriseDate);
        else
            return Optional.of(todayAt645Am);
    }

    private Instant todayAt645Am() {
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(clock), LocalTime.MIDNIGHT);
        dateTime = dateTime.plusHours(6);
        dateTime = dateTime.plusMinutes(45);
        return dateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    @Override
    public Optional<Instant> getPullDownDateTime() {
        return Optional.ofNullable(sunService.getSunsetDate(now(clock)));
    }

}
