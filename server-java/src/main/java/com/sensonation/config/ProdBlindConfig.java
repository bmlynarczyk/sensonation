package com.sensonation.config;

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
@Profile("prod")
public class ProdBlindConfig {

    @Bean
    GpioController gpio(){
        return GpioFactory.getInstance();
    }

    @Bean
    public McpOutputFactory mcpOutputFactory(GpioController gpio){
        return new McpOutputFactory(gpio);
    }

    @Bean
    public McpInputFactory mcpInputFactory(GpioController gpio){
        return new McpInputFactory(gpio);
    }

    @Bean
    @SneakyThrows
    public GpioProvider mcpA(){
        return new MCP23017GpioProvider(I2CBus.BUS_1, 0x20);
    }

    @Bean
    public Supplier<Map<String, Blind>> blindsProvider(McpOutputFactory mcpOutputFactory, McpInputFactory mcpInputFactory, GpioProvider mcpA){
        return new BlindsProvider(mcpOutputFactory, mcpInputFactory, mcpA);
    }

    @Bean
    public Supplier<Map<String, Consumer<Blind>>> blindActionsProvider(){
        return new BlindActionsProvider();
    }

    @Bean
    public BiConsumer<String, String> blindActionsExecutor(Supplier<Map<String, Blind>> blindsProvider, Supplier<Map<String, Consumer<Blind>>> blindActionsProvider){
        return new BlindActionsExecutor(blindsProvider, blindActionsProvider);
    }

}

