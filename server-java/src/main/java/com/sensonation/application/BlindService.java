package com.sensonation.application;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.sensonation.domain.BlindEvent;
import com.sensonation.domain.ManagedBlind;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
public class BlindService {

    private static final Function<String, ManagedBlind> THROW_NO_BLIND_FOUND = key -> {
        throw new NoSuchElementException("unknown blind");
    };

    private final Map<String, Consumer<String>> actionPublishers = ImmutableMap.of(
            "stop", (blindName) -> stopMovement(),
            "pullUp", this::auditedPublishBlindPullUp,
            "pullDown", this::auditedPublishBlindPullDown
    );

    private final Map<String, ManagedBlind> blinds;
    private final BlockingQueue<BlindEvent> blindEvents;
    private final BlindStopperService blindStopperService;

    public BlindService(Supplier<Map<String, ManagedBlind>> blindsProvider,
                        BlockingQueue<BlindEvent> blindEvents,
                        BlindStopperService blindStopperService) {
        this.blinds = blindsProvider.get();
        this.blindEvents = blindEvents;
        this.blindStopperService = blindStopperService;
    }

    public void executeFor(String blindName, String actionName) {
        if (!isSupported(actionName))
            throw new IllegalArgumentException("unsupported action");
        ManagedBlind blind = getBlind(blindName);
        confirmedExecuteFor(blind, actionName);
    }

    @SneakyThrows
    public void pullUpAllBlinds() {
        blinds.values()
                .stream()
                .filter(ManagedBlind::isActive)
                .map(ManagedBlind::getName)
                .forEach(this::publishBlindPullUp);
    }

    @SneakyThrows
    public void pullDownAllBlinds() {
        blinds.values()
                .stream()
                .filter(ManagedBlind::isActive)
                .map(ManagedBlind::getName)
                .forEach(this::publishBlindPullDown);
    }

    public void activate(String blindName) {
        ManagedBlind blind = blinds.computeIfAbsent(blindName, THROW_NO_BLIND_FOUND);
        blinds.replace(blind.getName(), ManagedBlind.builder().name(blind.getName()).active(true).build());
        log.info("blind {} has been activated", blind.getName());
    }

    public void deactivate(String blindName) {
        ManagedBlind blind = blinds.computeIfAbsent(blindName, THROW_NO_BLIND_FOUND);
        blinds.replace(blind.getName(), ManagedBlind.builder().name(blind.getName()).active(false).build());
        log.info("blind {} has been deactivate", blind.getName());
    }

    @SneakyThrows
    private void stopMovement() {
        log.info("stop blinds movement");
        blindEvents.drainTo(Lists.newArrayList());
        blindStopperService.stop();
    }

    @SneakyThrows
    private void auditedPublishBlindPullDown(String blindName) {
        log.info("call pull down action for blind {}", blindName);
        publishBlindEvent(blindName, "pullDown");
    }

    private void publishBlindPullDown(String blindName) {
        log.info("call pull down action for blind {}", blindName);
        publishBlindEvent(blindName, "pullDown");
    }

    @SneakyThrows
    private void auditedPublishBlindPullUp(String blindName) {
        log.info("call pull up action for blind {}", blindName);
        publishBlindEvent(blindName, "pullUp");

    }

    private void publishBlindPullUp(String blindName) {
        log.info("call pull up action for blind {}", blindName);
        publishBlindEvent(blindName, "pullUp");
    }

    private boolean isSupported(String actionName) {
        return actionPublishers.keySet().contains(actionName);
    }

    private ManagedBlind getBlind(String blindName) {
        return blinds.computeIfAbsent(blindName, THROW_NO_BLIND_FOUND);
    }

    private void confirmedExecuteFor(ManagedBlind blind, String actionName) {
        if (blind.isActive()) {
            actionPublishers.get(actionName).accept(blind.getName());
        } else {
            log.info("blind {} is inactive", blind.getName());
        }
    }

    @SneakyThrows
    private void publishBlindEvent(String blindName, String actionName) {
        blindEvents.put(BlindEvent.builder().blindName(blindName).actionName(actionName).build());
    }

}
