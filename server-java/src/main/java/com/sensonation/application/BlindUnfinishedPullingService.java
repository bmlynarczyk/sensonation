package com.sensonation.application;

import lombok.extern.slf4j.Slf4j;

import static java.util.Optional.of;

@Slf4j
public class BlindUnfinishedPullingService {

    private final BlindLimitSwitchCheckingService blindLimitSwitchCheckingService;
    private final BlindService blindService;

    public BlindUnfinishedPullingService(BlindLimitSwitchCheckingService blindLimitSwitchCheckingService, BlindService blindService) {
        this.blindLimitSwitchCheckingService = blindLimitSwitchCheckingService;
        this.blindService = blindService;
    }

    void pullDownUnfinishedBlind(String blindName) {
        of(blindLimitSwitchCheckingService.getState(blindName))
                .filter(blindLimitSwitchState -> !blindLimitSwitchState.isPullDownLimitReached())
                .ifPresent(blindLimitSwitchState -> blindService.publishBlindPullDown(blindName));
    }

    void pullUpUnfinishedBlind(String blindName) {
        of(blindLimitSwitchCheckingService.getState(blindName))
                .filter(blindLimitSwitchState -> !blindLimitSwitchState.isPullUpLimitReached())
                .ifPresent(blindLimitSwitchState -> blindService.publishBlindPullUp(blindName));
    }

}