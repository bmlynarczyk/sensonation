package com.sensonation.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.sleep;

@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Slf4j
public class BlindDriver {

    @Getter
    private final String name;
    private final McpOutput firstOutput;
    private final McpOutput secondOutput;
    private final McpInput pullDownLimitSwitch;
    private final McpInput pullUpLimitSwitch;
    private final AtomicBoolean stopped = new AtomicBoolean(true);

    @SneakyThrows
    public void pullDown(){
        log.info("pull down {}", name);
        stopped.set(false);
        firstOutput.setLow();
        sleep(500);
        secondOutput.setHigh();
        while (stillPullingDown() && !isStopped())
            sleep(50);
        stop();
    }

    @SneakyThrows
    public void pullUp() {
        log.info("pull up {}", name);
        stopped.set(false);
        secondOutput.setLow();
        sleep(500);
        firstOutput.setHigh();
        while (stillPullingUp() && !isStopped())
            sleep(50);
        stop();
    }

    public void stop(){
        log.info("stop movement of {}", name);
        firstOutput.setLow();
        secondOutput.setLow();
        stopped.set(true);
    }

    public boolean isPullDownLimitReached(){
        return !pullDownLimitSwitch.isOpen();
    }

    public boolean isPullUpLimitReached(){
        return !pullUpLimitSwitch.isOpen();
    }

    private boolean stillPullingDown() {
        return pullDownLimitSwitch.isOpen();
    }

    private boolean stillPullingUp() {
        return pullUpLimitSwitch.isOpen();
    }

    private boolean isStopped() {
        return stopped.get();
    }

}
