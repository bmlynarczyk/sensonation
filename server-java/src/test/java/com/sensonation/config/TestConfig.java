package com.sensonation.config;

import com.pi4j.io.gpio.GpioProvider;
import com.sensonation.application.BlindLimitSwitchCheckingService;
import com.sensonation.application.BlindLimitSwitchFactory;
import com.sensonation.application.McpOutputFactory;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

public class TestConfig {

    @Bean
    public McpOutputFactory mcpOutputFactory() {
        return mock(McpOutputFactory.class);
    }

    @Bean
    public BlindLimitSwitchFactory mcpInputFactory() {
        return mock(BlindLimitSwitchFactory.class);
    }

    @Bean
    @SneakyThrows
    public GpioProvider mcpA() {
        return mock(GpioProvider.class);
    }

    @Bean
    @SneakyThrows
    public GpioProvider mcpB() {
        return mock(GpioProvider.class);
    }

    @Bean
    public BlindLimitSwitchCheckingService blindLimitSwitchCheckingService(){
        return mock(BlindLimitSwitchCheckingService.class);
    }
}
