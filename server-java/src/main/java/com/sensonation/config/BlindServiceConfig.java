package com.sensonation.config;

import com.sensonation.application.*;
import com.sensonation.domain.BlindEvent;
import com.sensonation.domain.ManagedBlindsProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.TaskExecutor;

import java.time.Clock;
import java.util.concurrent.ArrayBlockingQueue;

@Configuration
public class BlindServiceConfig {

    @Bean
    public BlindStopperService blindStopperService(BlindDriversProvider blindDriversProvider) {
        return new BlindStopperService(blindDriversProvider);
    }

    @Bean
    @Profile("prod")
    public BlindLimitSwitchCheckingService blindLimitSwitchCheckingService(BlindDriversProvider blindDriversProvider,
                                                                           TaskExecutor blindTaskExecutor) {
        return new BlindLimitSwitchCheckingService(blindDriversProvider, Clock.systemDefaultZone(), blindTaskExecutor);
    }

    @Bean
    public BlindService blindService(ManagedBlindsProvider managedBlindsProvider,
                                     ArrayBlockingQueue<BlindEvent> blindEvents,
                                     BlindStopperService blindStopperService) {
        return new BlindService(managedBlindsProvider, blindEvents, blindStopperService);
    }

}
