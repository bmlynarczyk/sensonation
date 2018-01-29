package com.sensonation.config;

import com.sensonation.application.BlindService;
import com.sensonation.domain.ManagedBlindsProvider;
import com.sensonation.interfaces.BlindController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerConfig {

    @Bean
    public BlindController blindController(BlindService blindService, ManagedBlindsProvider managedBlindsProvider) {
        return new BlindController(blindService, managedBlindsProvider);
    }

}