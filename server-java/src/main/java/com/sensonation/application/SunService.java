package com.sensonation.application;

import java.time.Instant;

public interface SunService {

    Instant getSunriseDate(Instant date);

    Instant getSunsetDate(Instant date);

    boolean isBeforeSunrise();

    boolean isBeforeSunset();
}
