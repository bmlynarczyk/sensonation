package com.sensonation.application;

import com.sensonation.domain.BlindDriver;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import static java.lang.Thread.sleep;

@Slf4j
public class BlindLimitSwitchesMonitor {

    private static final String STARTED = "started";
    private static final String STOPPED = "stopped";

    private final BlindLimitSwitchesExpositor blindLimitSwitchesExpositor;
    private final AtomicBoolean started = new AtomicBoolean(true);
    private final Set<BlindDriver> blinds;

    public BlindLimitSwitchesMonitor(Supplier<Map<String, BlindDriver>> blindsProvider,
                                     TaskExecutor taskExecutor,
                                     BlindLimitSwitchesExpositor blindLimitSwitchesExpositor) {
        blinds = new HashSet<>(blindsProvider.get().values());
        this.blindLimitSwitchesExpositor = blindLimitSwitchesExpositor;
        taskExecutor.execute(this::run);
    }

    @SneakyThrows
    private void run(){
        while (true) {
            if(started.get())
                blinds.stream().forEach(blindLimitSwitchesExpositor::exposeState);
            sleep(250);
        }
    }

    public void switchMonitor() {
        boolean newValue = !started.get();
        log.info("blind limit switches monitor has been {}", newValue ? STARTED : STOPPED);
        started.set(newValue);
    }
}
