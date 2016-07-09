package com.sensonation.application;

import com.florianmski.suncalc.SunCalc;
import com.florianmski.suncalc.models.SunPhase;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;

public class SunService {

    private static final double latitude = 51.25;
    private static final double longitude = 22.5667;

    private final Clock clock;

    public SunService(Clock clock) {
        this.clock = clock;
    }

    public Instant getSunriseDate(Instant date) {
        GregorianCalendar calendar = toCalendar(date);
        SunPhase sunrise = SunCalc.getPhases(calendar, latitude, longitude).stream()
                .filter(sunPhase -> sunPhase.getName().toString().equals("Sunrise"))
                .findFirst()
                .get();
        return sunrise.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toInstant();
    }

    public Instant getSunsetDate(Instant date) {
        GregorianCalendar calendar = toCalendar(date);
        SunPhase sunset = SunCalc.getPhases(calendar, latitude, longitude).stream()
                .filter(sunPhase -> sunPhase.getName().toString().equals("Sunset"))
                .findFirst()
                .get();
        return sunset.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toInstant();
    }

    public boolean isBeforeSunrise() {
        Instant now = Instant.now(clock);
        return now.isBefore(getSunriseDate(now));
    }

    public boolean isBeforeSunset() {
        Instant now = Instant.now(clock);
        return now.isBefore(getSunsetDate(now));
    }

    private GregorianCalendar toCalendar(Instant instant) {
        return GregorianCalendar.from(ZonedDateTime.ofInstant(instant, ZoneId.systemDefault()));
    }
}
