package com.sensonation.config.dev;

import com.google.common.collect.ImmutableMap;
import com.sensonation.application.BlindService;
import com.sensonation.domain.BlindEvent;
import com.sensonation.domain.ManagedBlind;
import com.sensonation.interfaces.BlindController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Supplier;

@Configuration
@Profile("dev")
public class BlindConfig {

    @Bean
    public Supplier<Map<String, ManagedBlind>> managedBlindsProvider() {
        return ImmutableMap::of;
    }

    @Bean
    public ArrayBlockingQueue<BlindEvent> blindEvents() {
        return new ArrayBlockingQueue<>(20);
    }

    @Bean
    public BlindService blindService() {
        return new BlindService() {
            @Override
            public void executeFor(String blindName, String actionName) {

            }

            @Override
            public void pullUpAllBlinds() {

            }

            @Override
            public void pullDownAllBlinds() {

            }
        };
    }

    @Bean
    public BlindController blindController(BlindService blindService, Supplier<Map<String, ManagedBlind>> managedBlindsProvider) {
        return new BlindController(blindService, managedBlindsProvider);
    }

}

