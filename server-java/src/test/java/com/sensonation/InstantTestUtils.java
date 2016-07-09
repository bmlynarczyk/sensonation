package com.sensonation;

import java.time.Instant;
import java.time.LocalDateTime;

import static java.time.ZoneId.systemDefault;

public class InstantTestUtils {

    public static Instant getDate(int year, int month, int dayOfMonth, int hour, int minute) {
        return LocalDateTime.of(year, month, dayOfMonth, hour, minute).atZone(systemDefault()).toInstant();
    }

    public static Instant getDate(int year, int month, int dayOfMonth, int hour, int minute, int second, int nanoOfSecond) {
        return LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond).atZone(systemDefault()).toInstant();
    }

}
