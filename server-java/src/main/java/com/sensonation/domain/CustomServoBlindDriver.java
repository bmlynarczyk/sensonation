package com.sensonation.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.sleep;

@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Slf4j
public class CustomServoBlindDriver implements BlindDriver {
    @Getter
    private final String name;
    private final McpOutput firstOutput;
    private final McpOutput secondOutput;
    private final BlindLimitSwitch pullDownLimitSwitch;
    private final BlindLimitSwitch pullUpLimitSwitch;
    private final AtomicBoolean stopped = new AtomicBoolean(true);

    @Override
    @SneakyThrows
    public void pullDown() {
        if (!isPullDownLimitReached()) {
            log.info("pull down {}", name);
            stopped.set(false);
            firstOutput.setLow();
            sleep(500);
            secondOutput.setHigh();
            while (stillPullingDown() && !isStopped())
                sleep(50);
            stop();
        }
    }

    @Override
    @SneakyThrows
    public void pullUp() {
        if (!isPullUpLimitReached()) {
            log.info("pull up {}", name);
            stopped.set(false);
            secondOutput.setLow();
            sleep(500);
            firstOutput.setHigh();
            while (stillPullingUp() && !isStopped())
                sleep(50);
            stop();
        }
    }

    @Override
    public void stop() {
        log.info("stop movement of {}", name);
        firstOutput.setLow();
        secondOutput.setLow();
        stopped.set(true);
    }

    @Override
    public boolean isPullDownLimitReached() {
        return !pullDownLimitSwitch.isOpen();
    }

    @Override
    public boolean isPullUpLimitReached() {
        return !pullUpLimitSwitch.isOpen();
    }

    @Override
    public boolean stillPullingDown() {
        return pullDownLimitSwitch.isOpen();
    }

    @Override
    public boolean stillPullingUp() {
        return pullUpLimitSwitch.isOpen();
    }

    @Override
    public boolean isStopped() {
        return stopped.get();
    }
}
