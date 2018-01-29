package com.sensonation.config;

import com.pi4j.io.gpio.GpioProvider;
import com.sensonation.application.BlindDriversProvider;
import com.sensonation.application.BlindLimitSwitchFactory;
import com.sensonation.application.McpOutputFactory;
import com.sensonation.domain.ManagedBlindsProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class BlindConfig {

    @Bean
    public BlindDriversProvider blindDriversProvider(McpOutputFactory mcpOutputFactory,
                                                               BlindLimitSwitchFactory blindLimitSwitchFactory,
                                                               GpioProvider mcpA,
                                                               GpioProvider mcpB) {
        return new CustomServoBlindDriversProvider(mcpOutputFactory, blindLimitSwitchFactory, mcpA, mcpB);
    }

    @Bean
    public ManagedBlindsProvider managedBlindsProvider() {
        return new ManagedBlindsProvider();
    }

    @Bean
    public TaskExecutor blindTaskExecutor() {
        return new SimpleAsyncTaskExecutor("BlindTaskExecutor");
    }

}

