package com.sensonation.application;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.sensonation.domain.BlindEvent;
import com.sensonation.domain.ManagedBlind;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
public class BlindServiceImpl implements BlindService {

    private static final Function<String, ManagedBlind> THROW_NO_BLIND_FOUND = key -> {
        throw new NoSuchElementException("unknown blind");
    };
    private static final String PULL_UP = "pullUp";
    private static final String PULL_DOWN = "pullDown";
    private static final String STOP = "stop";
    private static final String DEACTIVATE = "deactivate";
    private static final String ACTIVATE = "activate";
    private static final Set<String> ACTIONS = ImmutableSet.of(ACTIVATE, DEACTIVATE, STOP, PULL_UP, PULL_DOWN);

    private final Map<String, ManagedBlind> blinds;
    private final BlockingQueue<BlindEvent> blindEvents;
    private final BlindStopper blindStopper;

    public BlindServiceImpl(Supplier<Map<String, ManagedBlind>> blindsProvider,
                            BlockingQueue<BlindEvent> blindEvents,
                            BlindStopper blindStopper) {
        this.blinds = blindsProvider.get();
        this.blindEvents = blindEvents;
        this.blindStopper = blindStopper;
    }

    @Override
    public void executeFor(String blindName, String actionName) {
        if(isNotSupported(actionName))
            throw new IllegalArgumentException("unsupported action");
        ManagedBlind blind = blinds.computeIfAbsent(blindName, THROW_NO_BLIND_FOUND);
        switchMonitor();
        confirmedExecuteFor(blind, actionName);
        switchMonitor();

    }

    @Override
    public void pullUpAllBlinds(){
        switchMonitor();
        blinds.values()
                .forEach(blind -> confirmedExecuteFor(blind, PULL_UP));
        switchMonitor();
    }

    @Override
    public void pullDownAllBlinds(){
        switchMonitor();
        blinds.values()
                .forEach(blind -> confirmedExecuteFor(blind, PULL_DOWN));
        switchMonitor();
    }

    @SneakyThrows
    private void switchMonitor() {
        blindEvents.put(BlindEvent.builder().actionName("switchMonitor").build());
    }

    private boolean isNotSupported(String actionName) {
        return !ACTIONS.contains(actionName);
    }

    private void confirmedExecuteFor(ManagedBlind blind, String actionName) {
        if (blind.isActive()) {
            if (STOP.equalsIgnoreCase(actionName)) {
                log.info("call {} for blind {}", actionName, blind.getName());
                stopMovement();
            } else if (DEACTIVATE.equalsIgnoreCase(actionName)){
                blinds.replace(blind.getName(), ManagedBlind.builder().name(blind.getName()).active(false).build());
                log.info("blind {} has been deactivated", blind.getName());
            } else {
                log.info("call {} for blind {}", actionName, blind.getName());
                produceBlindEvent(blind.getName(), actionName);
            }
        } else if(ACTIVATE.equalsIgnoreCase(actionName)) {
            blinds.replace(blind.getName(), ManagedBlind.builder().name(blind.getName()).active(true).build());
            log.info("blind {} has been activated", blind.getName());
        } else {
            log.info("blind {} is inactive", blind.getName());
        }
    }

    private void stopMovement() {
        blindEvents.drainTo(Lists.newArrayList());
        blindStopper.stop();
    }

    @SneakyThrows
    private void produceBlindEvent(String blindName, String actionName) {
        blindEvents.put(BlindEvent.builder().blindName(blindName).actionName(actionName).build());
    }

}
