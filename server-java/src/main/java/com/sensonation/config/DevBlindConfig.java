package com.sensonation.config;

import com.google.common.collect.ImmutableMap;
import com.pi4j.gpio.extension.mcp.MCP23017GpioProvider;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.i2c.I2CBus;
import com.sensonation.domain.Blind;
import com.sensonation.domain.BlindActionsExecutor;
import com.sensonation.domain.BlindActionsProvider;
import com.sensonation.domain.BlindsProvider;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Configuration
@Profile("dev")
public class DevBlindConfig {

    @Bean
    public Supplier<Map<String, Blind>> blindsProvider(){
        return ImmutableMap::of;
    }

    @Bean
    public BiConsumer<String, String> blindActionsExecutor(){
        return (s1, s2) -> {};
    }

}

