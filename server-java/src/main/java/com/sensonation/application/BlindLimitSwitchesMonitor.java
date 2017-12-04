package com.sensonation.application;

import com.sensonation.domain.BlindDriver;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.sleep;

@Slf4j
public class BlindLimitSwitchesMonitor {

    private static final String STARTED = "started";
    private static final String STOPPED = "stopped";

    private final BlindLimitSwitchesExpositor blindLimitSwitchesExpositor;
    private final AtomicBoolean started = new AtomicBoolean(true);
    private final Set<BlindDriver> blinds;
    private final BlindService blindService;

    public BlindLimitSwitchesMonitor(BlindDriversProvider blindsProvider,
                                     TaskExecutor taskExecutor,
                                     BlindLimitSwitchesExpositor blindLimitSwitchesExpositor, BlindService blindService) {
        blinds = new HashSet<>(blindsProvider.get().values());
        this.blindLimitSwitchesExpositor = blindLimitSwitchesExpositor;
        this.blindService = blindService;
        taskExecutor.execute(this::run);
    }

    @SneakyThrows
    private void run(){
        while (true) {
            if(started.get())
                blinds.forEach(blindLimitSwitchesExpositor::exposeState);
            sleep(250);
        }
    }

    void switchMonitor() {
        boolean newValue = !started.get();
        log.info("blind limit switches monitor has been {}", newValue ? STARTED : STOPPED);
        started.set(newValue);
    }

    void pullUpUnfinishedBlinds(){
        blindLimitSwitchesExpositor.getStates().stream()
                .filter(blindLimitSwitchState -> !blindLimitSwitchState.isPullUpLimitReached())
                .peek(blindLimitSwitchState -> blindService.publishBlindEvent(blindLimitSwitchState.getName(), "pullUp"))
                .findFirst()
                .ifPresent(blindLimitSwitchState -> blindService.pullUpUnfinishedBlinds());
    }

    void pullDownUnfinishedBlinds(){
        blindLimitSwitchesExpositor.getStates().stream()
                .filter(blindLimitSwitchState -> !blindLimitSwitchState.isPullDownLimitReached())
                .peek(blindLimitSwitchState -> blindService.publishBlindEvent(blindLimitSwitchState.getName(), "pullDown"))
                .findFirst()
                .ifPresent(blindLimitSwitchState -> blindService.pullDownUnfinishedBlinds());
    }

    void pullDownUnfinishedBlind(String blindName){
        if(!blindLimitSwitchesExpositor.getState(blindName).isPullDownLimitReached()){
            blindService.publishBlindEvent(blindName, "pullDown");
        }
    }

    void pullUpUnfinishedBlind(String blindName){
        if(!blindLimitSwitchesExpositor.getState(blindName).isPullUpLimitReached()){
            blindService.publishBlindEvent(blindName, "pullUp");
        }
    }

}
